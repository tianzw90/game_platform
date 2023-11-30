
package com.platform.system.user.service;


import com.platform.common.responseUtils.Result;
import com.platform.system.user.dto.UserLoginDto;
import com.platform.system.user.entity.SysUser;

public interface SysUserService {

    /**
     * 登录接口
     * @param userLoginDto 登录请求体
     * */
    Result userLogin(UserLoginDto userLoginDto);

    /**
     * 注册接口
     * @param sysUser 注册信息
     * */
    Result userRegister(SysUser sysUser);
}
