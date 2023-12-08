package com.unat.JWTcrudNLPcommBackend.service;

import com.unat.JWTcrudNLPcommBackend.dto.requests.PostCreateRequest;
import com.unat.JWTcrudNLPcommBackend.dto.requests.PostUpdateRequest;
import com.unat.JWTcrudNLPcommBackend.dto.responses.LikeResponse;
import com.unat.JWTcrudNLPcommBackend.dto.responses.PostResponse;
import com.unat.JWTcrudNLPcommBackend.entities.Post;
import com.unat.JWTcrudNLPcommBackend.entities.User;
import com.unat.JWTcrudNLPcommBackend.repos.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class PostService {
    private PostRepository postRepository;
    private LikeService likeService;
    private UserService userService;

    public PostService(PostRepository postRepository,
                       UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Autowired
    public void setLikeService(@Lazy LikeService likeService) {
        this.likeService = likeService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> list;
        if(userId.isPresent()) {
            list = postRepository.findByUserId(userId.get());
        }else
            list = postRepository.findAll();
        return list.stream().map(p -> {
                    List<LikeResponse> likes = likeService.getAllLikesWithParam(
                            Optional.ofNullable(null),
                            Optional.of(p.getId()));

                    return new PostResponse(p, likes);})
                .collect(Collectors.toList());
    }

    public Post getOnePostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public PostResponse getOnePostByIdWithLikes(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));
        return new PostResponse(post, likes);
    }

    public Post createOnePost(PostCreateRequest newPostRequest) {
        User user = userService.getOneUserById(newPostRequest.getUserId());
        if(user == null)
            return null;
        Post toSave = new Post();
        toSave.setId(newPostRequest.getId());
        toSave.setText(newPostRequest.getText());
        toSave.setTitle(newPostRequest.getTitle());
        toSave.setUser(user);
        toSave.setCreateDate(new Date());
        return postRepository.save(toSave);
    }

    public Post updateOnePostById(Long postId, PostUpdateRequest updatePost) {
        // check there is a post with given id
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            Post toUpdatePost = post.get();
            toUpdatePost.setText(updatePost.getText());
            toUpdatePost.setTitle(updatePost.getTitle());
            postRepository.save(toUpdatePost);
            return toUpdatePost;
        }
        return null;


    }

    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
    }
}

