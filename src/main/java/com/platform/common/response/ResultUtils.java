package com.platform.common.response;

public class ResultUtils {

    /**
     * 成功请求 (带提示信息和返回数据)
     * @param msg 提示信息
     * @param data 返回数据
     * */
    public static Result success(String msg, Object data) {
        Result result = new Result();
        result.setMessage(msg);
        result.setResult(data);
        result.setCode("0");
        return result;
    }

    /**
     * 成功请求 (只有提示,没有数据返回)
     * @param msg 提示信息
     * */
    public static Result success(String msg) {
        Result result = new Result();
        result.setMessage(msg);
        result.setCode("0");
        return result;
    }

    /**
     * 成功请求 (只有数据返回,没有提示)
     * @param data 返回数据
     * */
    public static Result success(Object data) {
        Result result = new Result();
        result.setResult(data);
        result.setCode("0");
        return result;
    }

    /**
     * 成功请求 (带提示信息)
     * @param msg 提示信息
     * */
    public static Result fail(String msg) {
        Result result = new Result();
        result.setMessage(msg);
        result.setCode("1");
        return result;
    }

}
