package com.platform.common.responseUtils;

public class ResultUtils {

    public static Result success(String msg) {
        Result result = new Result();
        result.setMessage(msg);
        result.setCode("0");
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.setMessage(msg);
        result.setCode("1");
        return result;
    }

}
