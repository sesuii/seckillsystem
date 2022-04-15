package com.jayce.seckillsystem.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtUtil {

    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param subject 加密信息
//     * @param ttlMillis jwt过期时间(毫秒)
     * @return
     */
    public static String createJWT(String subject) {
        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("token", subject);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS256, "123456");
//        if (ttlMillis >= 0) {
//            long expMillis = nowMillis + ttlMillis;
//            Date exp = new Date(expMillis);
//            // 设置过期时间
//            builder.setExpiration(exp);
//        }
        return builder.compact();
    }


    /**
     * Token的解密
     *
     * @param token  加密后的token
     * @return
     */
    public static Claims parseJWT(String token) {
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey("123456")
                .parseClaimsJws(token).getBody();
    }
}