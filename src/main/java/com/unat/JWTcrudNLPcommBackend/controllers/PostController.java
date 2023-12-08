package com.unat.JWTcrudNLPcommBackend.controllers;

import com.unat.JWTcrudNLPcommBackend.entities.Post;
import com.unat.JWTcrudNLPcommBackend.dto.requests.PostCreateRequest;
import com.unat.JWTcrudNLPcommBackend.dto.requests.PostUpdateRequest;
import com.unat.JWTcrudNLPcommBackend.dto.responses.PostResponse;
import com.unat.JWTcrudNLPcommBackend.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Optional<Long>: if there is a parameter, store it and get posts according to userID
    //                 otherwise add nothing to the mapping URI
    @GetMapping
    public List<PostResponse> getAllPosts(@RequestParam Optional<Long> userId) {
        return postService.getAllPosts(userId);
    }

    @PostMapping
    public Post createOnePost(@RequestBody PostCreateRequest newPostRequest) {
        return postService.createOnePost(newPostRequest);
    }

    @GetMapping("/{postId}")
    public PostResponse getOnePost(@PathVariable Long postId) {
        return postService.getOnePostByIdWithLikes(postId);
    }

    @PutMapping("/{postId}")
    public Post updateOnePost(@PathVariable Long postId,
                              @RequestBody PostUpdateRequest updatePost) {
        return postService.updateOnePostById(postId, updatePost);
    }

    @DeleteMapping("/{postId}")
    public void deleteOnePost(@PathVariable Long postId) {
        postService.deleteOnePostById(postId);
    }
}
