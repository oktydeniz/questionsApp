package com.question_app.demo.controller;

import com.question_app.demo.entities.RefreshToken;
import com.question_app.demo.entities.User;
import com.question_app.demo.request.RefreshRequest;
import com.question_app.demo.request.UserRequest;
import com.question_app.demo.response.AuthResponse;
import com.question_app.demo.security.JWDTokenProvider;
import com.question_app.demo.service.RefreshTokenService;
import com.question_app.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JWDTokenProvider jwdTokenProvider;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    private RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, JWDTokenProvider jwdTokenProvider, UserService userService, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwdTokenProvider = jwdTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserRequest userRequest){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userRequest.getUserName(), userRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthResponse authResponse = new AuthResponse();
        User user = userService.getOneUserByUserName(userRequest.getUserName());
        String tokenBearer = "Bearer " + jwdTokenProvider.generateJwtToken(authentication);
        String refresh = refreshTokenService.createRefreshToken(user);
        authResponse.setAccessToken(tokenBearer);
        authResponse.setRefreshToken(refresh);
        authResponse.setUserId(user.getId());
        return authResponse;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse > register(@RequestBody UserRequest userRequest){
        AuthResponse authResponse = new AuthResponse();
        if (userService.getOneUserByUserName(userRequest.getUserName()) != null){
            authResponse.setMessage("Username already in use !");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userService.save(user);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userRequest.getUserName(), userRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwdTokenProvider.generateJwtToken(authentication);
        authResponse.setAccessToken("Bearer " + jwtToken);
        authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
        authResponse.setUserId(user.getId());
        authResponse.setMessage("User successfully registered !");
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        AuthResponse authResponse = new AuthResponse();
        RefreshToken refreshToken = refreshTokenService.getByUser(request.getUserId());
        if (refreshToken.getToken().equals(request.getRefreshToken()) && !refreshTokenService.isRefreshExpired(refreshToken)) {
            User user = refreshToken.getUser();

            String jwtToken = jwdTokenProvider.generateJwtTokenByUserName(user.getId());
            authResponse.setAccessToken("Bearer " + jwtToken);
            authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
            authResponse.setUserId(user.getId());
            authResponse.setMessage("token successfully refreshed");
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } else {
            authResponse.setMessage("refresh token is not valid.");
            return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
        }
    }

}
