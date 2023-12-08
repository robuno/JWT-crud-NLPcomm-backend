package com.unat.JWTcrudNLPcommBackend.controllers;

import com.unat.JWTcrudNLPcommBackend.entities.RefreshToken;
import com.unat.JWTcrudNLPcommBackend.entities.User;
import com.unat.JWTcrudNLPcommBackend.dto.requests.RefreshRequest;
import com.unat.JWTcrudNLPcommBackend.dto.requests.UserCreateRequest;
import com.unat.JWTcrudNLPcommBackend.dto.responses.AuthResponse;
import com.unat.JWTcrudNLPcommBackend.security.JwtTokenProvider;
import com.unat.JWTcrudNLPcommBackend.service.RefreshTokenService;
import com.unat.JWTcrudNLPcommBackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserCreateRequest loginRequest) {

        // if username does not exist
        if(userService.getOneUserByUserName(loginRequest.getUserName()) == null) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("LoginUser_INV");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }

        // create Token with username and password
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword());

        // give authentication to the created token
        Authentication auth = authenticationManager.authenticate(authToken);
        // store auth object = principal
        SecurityContextHolder.getContext().setAuthentication(auth);
        // generate JWT token
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);
        // find user according to request username

        User user = userService.getOneUserByUserName(loginRequest.getUserName());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("LoginUser_OK");
        authResponse.setUserId(user.getId());
        authResponse.setAccessToken("Bearer " + jwtToken);
        authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserCreateRequest registerRequest) {

        // if username exists
        if(userService.getOneUserByUserName(registerRequest.getUserName()) != null) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Username is already in use!");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }


        // if username is free to use, create new User object w.r.t. Request
        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setUserEmail(registerRequest.getUserEmail());

        // date
        java.util.Date utilDate = new Date();
        java.sql.Date current_date = new java.sql.Date(utilDate.getTime());
        user.setRegisterDate(current_date);

        // role and avatar
        user.setUserRole("USER");
        user.setAvatarLink("avatar_0.jpg");

        // save created user to DB
        userService.saveOneUser(user);

        // login -------------------------------------------------------------------------------
        // create Token with username and password
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                registerRequest.getUserName(),
                registerRequest.getPassword()
        );

        // give authentication to the created token
        Authentication auth = authenticationManager.authenticate(authToken);
        // store auth object = principal
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);

        // fill Response object and send it
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("RegisterUser_OK");
        authResponse.setAccessToken("Bearer " + jwtToken);
        authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
        authResponse.setUserId(user.getId());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }


    // // when cli wants to refresh its token, fulfill its request
    // check whether cli's token == refresh token in DB
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        AuthResponse response = new AuthResponse();

        RefreshToken token = refreshTokenService.getUserById(refreshRequest.getUserId());
        if(token.getToken().equals(refreshRequest.getRefreshToken()) &&
                !refreshTokenService.isRefreshExpired(token)) {

            User user = token.getUser();
            String jwtToken = jwtTokenProvider.generateJwtTokenByUserId(user.getId());
            response.setMessage("RefreshToken_OK");
            response.setAccessToken("Bearer " + jwtToken);
            response.setUserId(user.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("RefreshToken_INV");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

    }







}
