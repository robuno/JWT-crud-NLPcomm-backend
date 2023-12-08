package com.unat.JWTcrudNLPcommBackend.service;

import com.unat.JWTcrudNLPcommBackend.dto.requests.UserUpdateRequest;
import com.unat.JWTcrudNLPcommBackend.dto.responses.UserResponse;
import com.unat.JWTcrudNLPcommBackend.entities.User;
import com.unat.JWTcrudNLPcommBackend.repos.LikeRepository;
import com.unat.JWTcrudNLPcommBackend.repos.PostRepository;
import com.unat.JWTcrudNLPcommBackend.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    UserRepository userRepository;
    PostRepository postRepository;
    LikeRepository likeRepository;

    public UserService(UserRepository userRepository,
                       PostRepository postRepository,
                       LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }


//    GET REQUESTS ------------------------------------------------------------------
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getOneUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    public UserResponse getOneUserResponseById(Long userId) {
        User foundUser = userRepository.findById(userId).orElse(null);
        return new UserResponse(foundUser);
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

//    SAVE & DELETE REQUESTS ------------------------------------------------------------
    public User saveOneUser(User newUser) {
        return userRepository.save(newUser);
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }


    //    UPDATE REQUESTS ---------------------------------------------------------------
    public User updateOneUser(Long userId, UserUpdateRequest newUser) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            User foundUser = user.get();
            foundUser.setUserName(newUser.getUserName());
            foundUser.setUserEmail(newUser.getUserEmail());
            foundUser.setAvatarLink(newUser.getAvatarLink());
            userRepository.save((foundUser));
            return foundUser;

        } else {
            return null;
        }
    }

    //    USER ACTIVITY ACTIONS ---------------------------
    public List<Object> getUserActivity(Long userId) {
        List<Long> postIds = postRepository.findTopByUserId(userId);
        if(postIds.isEmpty())
            return null;
        List<Object> likes = likeRepository.findUserLikesByPostId(postIds);
        List<Object> resultActions = new ArrayList<>();
        resultActions.addAll(likes);
        return resultActions;
    }




}
