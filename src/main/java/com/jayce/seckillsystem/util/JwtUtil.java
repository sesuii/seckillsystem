package com.jayce.seckillsystem.util;

import com.jayce.seckillsystem.constant.RedisConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author gerry
 */
public class JwtUtil {

    /**
     * 生成 jwt
     * 使用 Hs256 算法, 私匙使用固定秘钥 '123456'
     *
     * @param subject 加密信息
     * @return Token
     */
    public static String createJWT(String subject) {
        // 生成 JWT 的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Map<String, Object> claims = new HashMap<>(16);
        claims.put("token", subject);
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setSubject(subject)
                .setExpiration(new Date(nowMillis + RedisConstant.TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, "123456");
        return builder.compact();
    }

    /**
     * Token的解密
     *
     * @param token 加密后的 token
     * @return
     */
    public static Claims parseJWT(String token) {
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey("123456")
                .parseClaimsJws(token).getBody();
    }
}