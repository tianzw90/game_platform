package com.platform.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.platform.common.comstant.CommonConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Map;

public class JwtUtils {


    /**
     * 生成token
     * @param userId 登录用户ID
     * */
    public static String generateJwtToken(String userId) {
        return JWT.create()
                .withClaim("userId", userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                .sign(Algorithm.HMAC256(CommonConstant.jwt_secret_key));
    }


    /**
     * 校验token
     * @param token token令牌
     * */
    public static String verifyJwtToken(String token) {
        return JWT.require(Algorithm.HMAC256(CommonConstant.jwt_secret_key))
                .build()
                .verify(token)
                .getClaims()
                .get("userId")
                .asString();
    }

}
