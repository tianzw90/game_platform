package com.platform.common.comstant;

import java.util.List;

public class CommonConstant {

    /**
     * 倒序
     */
    public static final String ORDER_DESC="DESC";
    /**
     * 正序
     */
    public static final String ORDER_ASC="ASC";

    /**
     * jwt密钥
     * */
    public final static String jwt_secret_key = "game-secret";

    /**
     * redis 登录key
     * */
    public final static String redis_key_user_login = "login:userId:";

    /**
     * redis 登录过期时间
     * 单位秒
     * 12小时
     * */
    public final static int redis_key_user_login_expire_time = 60 * 60 * 12;

}
