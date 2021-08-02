package com.bcy.oauth2.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtils {


    private static final String secret = "*&ycy!yyds?";

    private static final long expiration = 1000 * 3600L;

    //创建token
    public static String createToken(String id, String password){
        JwtBuilder jwtBuilder = Jwts.builder()
                //设置唯一id
                .setId(id)
                .setSubject(password)
                //签发时间
                .setIssuedAt(new Date())
                //一小时过期
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256,secret);
        log.info("创建token成功，用户：" + id + " token：" + jwtBuilder.compact());
        return jwtBuilder.compact();
    }

    //获取用户名
    public static String getUsername(String token){
        return getTokenBody(token).getId();
    }

    //解析token
    public static Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

}