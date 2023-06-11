package com.question_app.demo.service;

import com.question_app.demo.entities.User;
import com.question_app.demo.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User findUser(Long userId) {
        return this.userRepository.findById(userId).orElse(null);
    }

    public User updateUser(Long userId, User currentUser) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setUserName(currentUser.getUserName());
            foundUser.setPassword(currentUser.getPassword());
            this.userRepository.save(foundUser);
            return foundUser;
        }
        return null;
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
