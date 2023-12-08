
package com.platform.system.user.service;


import com.platform.common.response.Result;
import com.platform.common.service.BaseService;
import com.platform.system.user.dto.UserLoginDto;
import com.platform.system.user.entity.SysUser;

public interface SysUserService extends BaseService<SysUser> {

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

    /**
     * 获取当前人
     * */
    Result userInfo();

    /**
     * 退出登录
     * @param sysUser 要退出的用户
     * */
    Result userLoginOut(SysUser sysUser);
}
