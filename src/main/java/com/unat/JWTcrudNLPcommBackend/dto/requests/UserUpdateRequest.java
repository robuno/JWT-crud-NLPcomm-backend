package com.unat.JWTcrudNLPcommBackend.dto.requests;

import lombok.Data;

@Data
public class UserUpdateRequest {
    String userName;
    String userEmail;
    String avatarLink;
}
