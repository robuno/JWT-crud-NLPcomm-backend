package com.unat.JWTcrudNLPcommBackend.dto.requests;

import lombok.Data;

@Data
public class UserCreateRequest {

    String userName;
    String password;
    String userEmail;

}