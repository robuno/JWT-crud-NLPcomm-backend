package com.unat.JWTcrudNLPcommBackend.dto.responses;

import com.unat.JWTcrudNLPcommBackend.entities.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserResponse {

    Long id;
    String userName;
    String userEmail;
    String password;
    String userRole;
    String avatarLink;
    Date registerDate;

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.userName = entity.getUserName();
        this.userEmail = entity.getUserEmail();
        this.password = entity.getPassword();
        this.userRole = entity.getUserRole();
        this.avatarLink = entity.getAvatarLink();
        this.registerDate = entity.getRegisterDate();
    }
}
