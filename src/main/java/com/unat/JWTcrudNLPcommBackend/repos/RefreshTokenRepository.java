package com.unat.JWTcrudNLPcommBackend.repos;

import com.unat.JWTcrudNLPcommBackend.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUserId(Long userId);
}
