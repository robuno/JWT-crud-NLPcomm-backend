package com.unat.JWTcrudNLPcommBackend.controllers;

import com.unat.JWTcrudNLPcommBackend.dto.requests.LikeCreateRequest;
import com.unat.JWTcrudNLPcommBackend.dto.responses.LikeResponse;
import com.unat.JWTcrudNLPcommBackend.entities.Like;
import com.unat.JWTcrudNLPcommBackend.service.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {
    private LikeService likeService;
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }



    //------------------------ GET MAPPINGS
    @GetMapping
    public List<LikeResponse> getAllLikes(@RequestParam Optional<Long> userId,
                                          @RequestParam Optional<Long> postId) {
        return likeService.getAllLikesWithParam(userId, postId);
    }

    @PostMapping
    public Like createOneLike(@RequestBody LikeCreateRequest request) {
        return likeService.createOneLike(request);
    }

    @GetMapping("/{likeId}")
    public Like getOneLike(@PathVariable Long likeId) {
        return likeService.getOneLikeById(likeId);
    }

    @DeleteMapping("/{likeId}")
    public void deleteOneLike(@PathVariable Long likeId) {
        likeService.deleteOneLikeById(likeId);
    }




}