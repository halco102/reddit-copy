package com.project.reddit.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt-secret-key}")
    private String secret;

    private Claims customClaims(CustomUserDetailsImp imp) {
        Claims claims = new DefaultClaims();
        Map<String, String> map = new HashMap<>();

        map.put("username", imp.getUsername());
        map.put("email", imp.getEmail());
        map.put("imageUrl", imp.getImageUrl());
        map.put("roles", imp.getAuthorities().toString());
        claims.putAll(map);
        claims.setSubject(String.valueOf(imp.getId()));
        return claims;
    }
    public String generateJwtToken(Authentication authentication) {
        CustomUserDetailsImp customUserDetailsImp = (CustomUserDetailsImp) authentication.getPrincipal();
        return Jwts.builder()
                .setClaims(customClaims(customUserDetailsImp))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getUsernameByJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.get("username").toString();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (SignatureException e) {
            log.error(e.getMessage());
        }catch (MalformedJwtException e) {
            log.error(e.getMessage());
        }catch (ExpiredJwtException e) {
            log.error(e.getMessage());
        }catch (UnsupportedJwtException e) {
            log.error(e.getMessage());
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
        }
        return false;
    }

}
