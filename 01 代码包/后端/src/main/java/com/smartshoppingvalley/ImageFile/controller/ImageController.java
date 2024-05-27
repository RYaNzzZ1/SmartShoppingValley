package com.smartshoppingvalley.ImageFile.controller;


import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/ImageFile/image")
public class ImageController {

    @Value("${letao.path}")
    private String path;

    //上传文件
    @PostMapping("/upload")
    public Result upload(MultipartFile[] files) {
        log.info("上传文件");

        //文件夹不存在则创建
        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();

        List<String> filenames = new ArrayList<>();

        log.info("size = {}", files.length);

        for (MultipartFile multipartFile : files) {

            //文件原始名
            String originalFilename = multipartFile.getOriginalFilename();
            //后缀（文件类型）
            String suffix = null;
            if (originalFilename != null) {
                suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            //UUID重新生成文件名
            String filename = UUID.randomUUID() + suffix;
            filenames.add(filename);

            try {
                multipartFile.transferTo(new File(path + filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Result.success(filenames);
    }

    //下载文件
    @GetMapping("/download")
    public void download(String url, HttpServletResponse response) {
        log.info("下载文件：{}", url);

        try {
            //输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(path + url);
            //输出流写会浏览器
            ServletOutputStream responseOutputStream = response.getOutputStream();

            //response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];

            while (len != -1) {
                len = fileInputStream.read(bytes);
                responseOutputStream.write(bytes, 0, len);
                responseOutputStream.flush();
            }

            fileInputStream.close();
            responseOutputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
