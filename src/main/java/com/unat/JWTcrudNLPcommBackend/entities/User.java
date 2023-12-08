package com.unat.JWTcrudNLPcommBackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity // class will be mapped to DB
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String userName;
    String userEmail;
    String password;
    String userRole;
    String avatarLink;


    @Temporal(TemporalType.TIMESTAMP)
    Date registerDate;

}
