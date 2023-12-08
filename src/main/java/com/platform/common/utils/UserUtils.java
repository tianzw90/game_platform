package com.platform.common.utils;

import com.platform.common.threadLocal.ThreadLocalUtil;
import com.platform.system.user.entity.SysUser;
import org.springframework.util.ObjectUtils;

public class UserUtils {

    public static SysUser getCurrUserInfo(){
        SysUser sysUser = ThreadLocalUtil.get();
        if (ObjectUtils.isEmpty(sysUser)) {
            return null;
        } else {
            return sysUser;
        }
    }
}
