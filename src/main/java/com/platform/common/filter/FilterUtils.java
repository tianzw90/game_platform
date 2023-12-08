package com.platform.common.filter;

import com.platform.common.comstant.CommonConstant;
import com.platform.common.jwt.JwtUtils;
import com.platform.common.redis.RedisUtils;
import com.platform.common.threadLocal.ThreadLocalUtil;
import com.platform.system.user.entity.SysUser;
import com.platform.system.user.service.SysUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class FilterUtils implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FilterUtils.class);

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private SysUserService sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取token
        String token = request.getHeader("Authorization");
        try {
            String method = request.getMethod();
            String requestUrl = request.getRequestURI();
            if (requestUrl.contains("api-docs") || requestUrl.contains("swagger")) {
                return true;
            }
            String userId = JwtUtils.verifyJwtToken(token);
            Object redisToken = redisUtils.get(CommonConstant.redis_key_user_login + userId);
            // 验证redis中的token是否已经失效 或者  是否与请求携带的token一致
            if (ObjectUtils.isEmpty(redisToken) || !token.equals(redisToken)) {
                // 不一致  抛出异常
                throw new RuntimeException();
            }
            // 验证通过 将user存入threadLocal中 方便直接取
            SysUser sysUser = sysUserService.getById(userId);
            ThreadLocalUtil.set(sysUser);
            return true;
        }catch (Exception e) {
            logger.error("未登录，没有权限操作。");
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
