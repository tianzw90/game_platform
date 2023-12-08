package com.platform.system.user.service.impl;

import com.platform.common.comstant.CommonConstant;
import com.platform.common.encryptionUtils.Md5Util;
import com.platform.common.jwt.JwtUtils;
import com.platform.common.redis.RedisUtils;
import com.platform.common.response.Result;
import com.platform.common.response.ResultUtils;
import com.platform.common.service.BaseServiceImpl;
import com.platform.common.threadLocal.ThreadLocalUtil;
import com.platform.system.user.dto.UserLoginDto;
import com.platform.system.user.mapper.SysUserMapper;
import com.platform.system.user.entity.SysUser;
import com.platform.system.user.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;

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
            // 登录成功 生成token 先验证redis是否已经生成过
            String token = "";
            String redisKey = CommonConstant.redis_key_user_login + user.getId();
            boolean bool = redisUtils.hasKey(redisKey);
            if (bool) {
                token = redisUtils.get(CommonConstant.redis_key_user_login + user.getId()).toString();
            } else {
                token = JwtUtils.generateJwtToken(user.getId());
                // 将生成的token存储到redis中 过期时间12小时
                redisUtils.set(redisKey, token, CommonConstant.redis_key_user_login_expire_time);
            }
            return ResultUtils.success("登陆成功", token);
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
            result = ResultUtils.success("注册成功。", null);
            result.setResult(userMapper.getByUserName(sysUser.getUserName()));
        }
        return result;
    }

    /**
     * 获取当前人
     * */
    @Override
    public Result userInfo() {
        SysUser sysUser = ThreadLocalUtil.get();
        if (ObjectUtils.isEmpty(sysUser)) {
            return ResultUtils.fail("未登录，无法获取当前人信息。");
        }
        return ResultUtils.success("获取当前人信息成功。", sysUser);
    }

    /**
     * 退出登录
     * @param sysUser 要退出的用户
     * */
    @Override
    public Result userLoginOut(SysUser sysUser) {
        try {
            // 获取userId
            String userId = sysUser.getId();
            // 清除redis
            redisUtils.del(CommonConstant.redis_key_user_login + userId);
            // 清除threadLocal
            ThreadLocalUtil.remove();
            return ResultUtils.success("退出登录成功。", null);
        }catch (Exception e) {
            return ResultUtils.fail("退出登失败。");
        }
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
        return ResultUtils.success("校验通过", null);
    }
}
