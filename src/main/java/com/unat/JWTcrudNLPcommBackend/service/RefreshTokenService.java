package com.unat.JWTcrudNLPcommBackend.service;


import com.unat.JWTcrudNLPcommBackend.entities.RefreshToken;
import com.unat.JWTcrudNLPcommBackend.entities.User;
import com.unat.JWTcrudNLPcommBackend.repos.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${refresh.token.expires.in}")
    Long expireSeconds;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }


    public String createRefreshToken(User user) {
        // get refresh token of selected user
        RefreshToken refToken = refreshTokenRepository.findByUserId(user.getId());

        // if it is the first refresh token of the user, create it
        if(refToken == null) {
            refToken =	new RefreshToken();
            refToken.setUser(user);
        }

        // 128 bit unique identifier
        refToken.setToken(UUID.randomUUID().toString());
        refToken.setExpireDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
        refreshTokenRepository.save(refToken);
        return refToken.getToken();
    }

    public boolean isRefreshExpired(RefreshToken token) {
        // if token expire date is before current date, return True = expired
        return token.getExpireDate().before(new Date());
    }

    public RefreshToken getUserById(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }
}
