package com.platform.system.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.encryptionUtils.Md5Util;
import com.platform.common.responseUtils.Result;
import com.platform.common.responseUtils.ResultUtils;
import com.platform.system.user.dto.UserLoginDto;
import com.platform.system.user.mapper.SysUserMapper;
import com.platform.system.user.entity.SysUser;
import com.platform.system.user.service.SysUserService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


@Service
public class SysUserServiceImpl implements SysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 登录接口
     * @param userLoginDto 登录请求体
     * */
    @Override
    public Result userLogin(UserLoginDto userLoginDto) {
        SysUser user = userMapper.getByUserName(userLoginDto.getUserName());
        if (ObjectUtils.isEmpty(user)) {
            return ResultUtils.fail("用户不存在，请注册后再登录。");
        } else {
            String passWord = Md5Util.getMD5String(userLoginDto.getPassWord());
            if (!passWord.equals(user.getPassWord())) {
                return ResultUtils.fail("密码不正确，请重新输入。");
            }
            return ResultUtils.success("登陆成功");
        }
    }

    /**
     * 注册接口
     * @param sysUser 注册信息
     * */
    @Override
    public Result userRegister(SysUser sysUser) {
        Result result = checkUserRegister(sysUser);
        // 验证通过  保存用户
        if (result.getCode().equals("0")) {
            sysUser.setPassWord(Md5Util.getMD5String(sysUser.getPassWord()));
            userMapper.insert(sysUser);
            result = ResultUtils.success("注册成功。");
            result.setResult(userMapper.getByUserName(sysUser.getUserName()));
        }
        return result;
    }

    /**
     * 校验用户是否被注册过
     * */
    public Result checkUserRegister(SysUser sysUser) {
        SysUser user = new SysUser();
        // 用户名校验
        String userName = sysUser.getUserName();
        if (ObjectUtils.isEmpty(userName)) {
            return ResultUtils.fail("用户名不能为空。");
        }
        user = userMapper.getByUserName(userName);
        if (!ObjectUtils.isEmpty(user)) {
            return ResultUtils.fail("用户名已被注册。");
        }
        // 手机号校验
        String mobile = sysUser.getMobile();
        if (ObjectUtils.isEmpty(mobile)) {
            return ResultUtils.fail("手机号不能为空。");
        }
        user = userMapper.getByMobile(mobile);
        if (!ObjectUtils.isEmpty(user)) {
            return ResultUtils.fail("手机号已被注册。");
        }
        // 身份证号校验
        String cardNum = sysUser.getCardNum();
        if (ObjectUtils.isEmpty(cardNum)) {
            return ResultUtils.fail("身份证号不能为空。");
        }
        user = userMapper.getByCardNum(cardNum);
        if (!ObjectUtils.isEmpty(user)) {
            return ResultUtils.fail("身份证号已被注册。");
        }
        // 邮箱校验
        String email = sysUser.getEmail();
        if (ObjectUtils.isEmpty(cardNum)) {
            return ResultUtils.fail("邮箱不能为空。");
        }
        user = userMapper.getByEmail(email);
        if (!ObjectUtils.isEmpty(user)) {
            return ResultUtils.fail("邮箱已被注册。");
        }
        return ResultUtils.success("校验通过");
    }
}
