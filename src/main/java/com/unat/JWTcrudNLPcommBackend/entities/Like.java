package com.unat.JWTcrudNLPcommBackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="p_like")
@Data
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY) // many likes to one post
    // When we got the Like obj, we don't need immediately Post obj
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // when user is deleted, posts must be deleted
    @JsonIgnore
    Post post;

    @ManyToOne(fetch = FetchType.LAZY) // many likes to one user
    // When we got the Like obj, we don't need immediately User obj
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // when user is deleted, posts must be deleted
    @JsonIgnore
    User user;

}
