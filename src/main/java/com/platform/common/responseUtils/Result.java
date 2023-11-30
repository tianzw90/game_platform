package com.platform.common.responseUtils;

import lombok.Data;

@Data
public class Result {

    private String message;

    private Object result;

    private String code;

}
