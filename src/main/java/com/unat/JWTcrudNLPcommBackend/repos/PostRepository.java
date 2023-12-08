package com.unat.JWTcrudNLPcommBackend.repos;


import com.unat.JWTcrudNLPcommBackend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // create custom methods like findByX by using JPA's built-in interface
    List<Post> findByUserId(Long userId);

    @Query(value = "SELECT ID FROM post WHERE user_id = :userId " +
            "ORDER BY create_date " +
            "DESC LIMIT 5",
            nativeQuery = true)
    List<Long> findTopByUserId(@Param("userId") Long userId);
}
