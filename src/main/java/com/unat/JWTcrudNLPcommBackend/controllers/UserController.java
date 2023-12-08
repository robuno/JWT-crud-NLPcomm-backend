package com.unat.JWTcrudNLPcommBackend.controllers;

import com.unat.JWTcrudNLPcommBackend.dto.requests.UserUpdateRequest;
import com.unat.JWTcrudNLPcommBackend.entities.User;
import com.unat.JWTcrudNLPcommBackend.exceptions.UserNotFoundException;
import com.unat.JWTcrudNLPcommBackend.dto.responses.UserResponse;
import com.unat.JWTcrudNLPcommBackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(u -> new UserResponse(u))
                .toList();
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody User newUser) {
        User user = userService.saveOneUser(newUser);
        if (user != null)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/{userId}")
    public UserResponse getOneUser(@PathVariable Long userId) {
       return userService.getOneUserResponseById(userId);
    }

    // change selected user (with ID) and get info related to this user
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateOneUser(@PathVariable Long userId, @RequestBody UserUpdateRequest updateUser) {
        User user = userService.updateOneUser(userId, updateUser);
        if(user != null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @DeleteMapping("/{userId}")
    public void deleteOneUser(@PathVariable Long userId) {
        userService.deleteById(userId);
    }

    // ------ GET USER ACTIVITY
    @GetMapping("/activity/{userId}")
    public List<Object> getUserActivity(@PathVariable Long userId) {
        return userService.getUserActivity(userId);
    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void handleUserNotFound() {

    }
}