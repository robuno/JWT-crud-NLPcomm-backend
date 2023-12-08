package com.unat.JWTcrudNLPcommBackend.dto.responses;

import com.unat.JWTcrudNLPcommBackend.entities.Post;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostResponse {

    Long id;
    Long userId;
    String userName;
    String title;
    String text;
    Date createDate;
    List<LikeResponse> postLikes;

    public PostResponse(Post entity, List<LikeResponse> likes) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getUserName();
        this.title = entity.getTitle();
        this.text = entity.getText();
        this.createDate = entity.getCreateDate();
        this.postLikes = likes;
    }

}