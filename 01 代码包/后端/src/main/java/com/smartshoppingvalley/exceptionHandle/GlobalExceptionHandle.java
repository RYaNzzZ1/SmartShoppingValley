package com.smartshoppingvalley.exceptionHandle;

import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

//全局异常处理
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {

    //异常处理
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result exceptionHandle(SQLIntegrityConstraintViolationException exception) {
        String exceptionMessage = exception.getMessage();
        log.error(exceptionMessage);

        if (exceptionMessage.contains("Duplicate entry")) {
            String[] split = exceptionMessage.split(" ");
            log.info(exceptionMessage);
            return Result.error(split[2] + "已存在");
        }

        return Result.error(exceptionMessage);
    }
}
