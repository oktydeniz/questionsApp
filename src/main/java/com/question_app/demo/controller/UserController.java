package com.question_app.demo.controller;

import com.question_app.demo.entities.User;
import com.question_app.demo.repo.UserRepository;
import com.question_app.demo.response.UserResponse;
import com.question_app.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return this.userService.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return this.userService.save(user);
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable Long userId) {
        return new UserResponse(this.userService.findUser(userId));
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User currentUser) {
        return userService.updateUser(userId, currentUser);
    }

    @GetMapping("/activity/{userId}")
    public List<Object> getUserActivity(@PathVariable Long userId){
        return userService.getUserActivity(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        this.userService.deleteUser(userId);
    }
}
