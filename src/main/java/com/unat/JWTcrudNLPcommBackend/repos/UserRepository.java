package com.unat.JWTcrudNLPcommBackend.repos;

import com.unat.JWTcrudNLPcommBackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String username);
}

