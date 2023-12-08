package com.unat.JWTcrudNLPcommBackend.dto.requests;

import lombok.Data;

@Data
public class LikeCreateRequest {

    Long id;
    Long userId;
    Long postId;

}
