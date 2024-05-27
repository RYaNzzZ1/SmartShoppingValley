package com.smartshoppingvalley.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private Object data; //数据

    //private Map map = new HashMap(); //动态数据

    public static Result success() {

        return new Result(200, "success", null);
    }

    public static Result success(Object object) {

        return new Result(200, "success", object);
    }

    public static Result error(String msg) {

        return new Result(500, msg, null);
    }


}
