package com.question_app.demo.controller;

import com.question_app.demo.entities.User;
import com.question_app.demo.request.UserRequest;
import com.question_app.demo.security.JWDTokenProvider;
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

    public AuthController(AuthenticationManager authenticationManager, JWDTokenProvider jwdTokenProvider, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwdTokenProvider = jwdTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserRequest userRequest){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userRequest.getUserName(), userRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "Bearer " + jwdTokenProvider.generateJwtToken(authentication);
    }

    @PostMapping("/register")
    public ResponseEntity<String > register(@RequestBody UserRequest userRequest){
        if (userService.getOneUserByUserName(userRequest.getUserName()) != null){
            return new ResponseEntity<>("Username already in use !", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userService.save(user);

        return new ResponseEntity<>("User successfully registered !", HttpStatus.CREATED);
    }

}