package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${example.app.secret}")
    private String APP_SECRET;

    @Value("${questapp.expires.in}")
    private long EXPIRES_IN;

    public String generateJwtToken(Authentication auth) {
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);


        Key key = Keys.hmacShaKeyFor(APP_SECRET.getBytes());

        return Jwts.builder()
                .setSubject(Long.toString(userDetails. getId()))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
    }

    public Long getUserIdFromJwt(String token) {
        Key key = Keys.hmacShaKeyFor(APP_SECRET.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(APP_SECRET.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}