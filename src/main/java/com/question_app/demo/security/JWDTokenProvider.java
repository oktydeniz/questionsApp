package com.question_app.demo.security;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWDTokenProvider {

    @Value("${questapp.app.secret}")
    private String APP_SECRET;
    @Value("${questapp.expires.in}")
    private Long EXPIRES_IN;

    public String generateJwtToken(Authentication authentication) {
        JWTUserDetail userDetail = (JWTUserDetail) authentication.getPrincipal();
        Date expiredDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder().setSubject(Long.toString(userDetail.getId()))
                .setIssuedAt(new Date()).setExpiration(expiredDate).
                signWith(SignatureAlgorithm.HS256, APP_SECRET).compact();
    }

    Long getUserIdFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
            return  !isTokenExpired(token);
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | IllegalArgumentException |
                 UnsupportedJwtException e){
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date exp = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody().getExpiration();
        return  exp.before(new Date());

    }

}