package com.unat.JWTcrudNLPcommBackend.dto.requests;

import lombok.Data;

@Data
public class RefreshRequest {

    Long userId;
    String refreshToken;
}