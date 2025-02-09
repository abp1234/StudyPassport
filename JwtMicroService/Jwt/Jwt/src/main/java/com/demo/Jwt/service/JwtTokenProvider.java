package com.demo.Jwt.service;

import io.jsonwebtoken.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class JwtTokenProvider {

    private final String SECRET_KEY = Base64.getEncoder().encodeToString("tempSecretKey".getBytes());
    private final long VALIDITY_IN_MS = 3600000;

    public String createToken(String username){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+VALIDITY_IN_MS);

        String token =Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        System.out.println(token);
        return token;
    }

    public String refreshToken(String token){
        try{
//            Claims claims = Jwts.parser()
//                    .setSigningKey(SECRET_KEY)
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            String username = claims.getSubject();
//            if(username == null || username.isEmpty()){
//                throw new RuntimeException("");
//            }
            String username ="NKJ";
            return createToken(username);
        } catch (JwtException e){
            throw new RuntimeException("token 이상",e);
        }
    }
}
