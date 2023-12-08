package com.unat.JWTcrudNLPcommBackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${crudwithjwt.app.secret}")
    private String APP_SECRET;
    @Value("${crudwithjwt.expires.in}")
    private long EXPIRES_IN;

    public String generateJwtToken(Authentication auth) {
        // user will be authenticated
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();

        // when token is created
        Date issueDate =new Date();
        // when token will be expired
        Date expireDate = new Date(new Date().getTime() + 120000);
        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getId()))     // user
                .setIssuedAt(issueDate)                             // key generation date
                .setExpiration(expireDate)                          // expiration date
                .signWith(getSignKey(), SignatureAlgorithm.HS256)     // token
                .compact();
    }

    public String generateJwtTokenByUserId(Long userId) {

        Date issueDate =new Date();
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder()
                .setSubject(Long.toString(userId))     // user
                .setIssuedAt(issueDate)                             // key generation date
                .setExpiration(expireDate)                          // expiration date
                .signWith(getSignKey(), SignatureAlgorithm.HS256)     // token
                .compact();

    }


    // decrypt token to get user id
    Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(APP_SECRET)              // give key for parsing
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    boolean validateToken(String token) {
        try {
            // if token can be parsed it is ok
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
            return !isTokenExpired(token);

        } catch (MalformedJwtException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(APP_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        // is today's date before expiration date
        return expiration.before(new Date());
    }

    private Key getSignKey() {
        byte[] key = Decoders.BASE64.decode(APP_SECRET);
        return Keys.hmacShaKeyFor(key);
    }



}
