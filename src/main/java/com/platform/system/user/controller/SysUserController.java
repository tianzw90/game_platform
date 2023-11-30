package com.platform.system.user.controller;

import com.platform.common.responseUtils.Result;
import com.platform.system.user.dto.UserLoginDto;
import com.platform.system.user.entity.SysUser;
import com.platform.system.user.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.bind.annotation.*;


@Configuration
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    private static final Logger logger= LoggerFactory.getLogger(SysUserController.class);



    /**
     * 注册接口
     * @param sysUser 注册信息
     * */
    @PostMapping("userRegister")
    public Result userRegister(@RequestBody SysUser sysUser) {
        return sysUserService.userRegister(sysUser);
    }

    /**
     * 登录接口
     * @param userLoginDto 登录请求体
     * */
    @PostMapping("userLogin")
    public Result userLogin(@RequestBody UserLoginDto userLoginDto) {
        return sysUserService.userLogin(userLoginDto);
    }


}
