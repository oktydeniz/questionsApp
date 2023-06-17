package com.question_app.demo.service;

import com.question_app.demo.entities.Comment;
import com.question_app.demo.entities.Like;
import com.question_app.demo.entities.User;
import com.question_app.demo.repo.CommentRepository;
import com.question_app.demo.repo.LikeRepository;
import com.question_app.demo.repo.PostRepository;
import com.question_app.demo.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    LikeRepository likeRepository;
    CommentRepository commentRepository;
    PostRepository postRepository;

    public UserService(UserRepository userRepository,
                       LikeRepository likeRepository,
                       CommentRepository commentRepository,
                       PostRepository postRepository) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
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
            foundUser.setAvatar(currentUser.getAvatar());
            this.userRepository.save(foundUser);
            return foundUser;
        }
        return null;
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public List<Object> getUserActivity(Long userId) {
       List<Long> postIds =  postRepository.findTopByUserId(userId);
       if (postIds.isEmpty()) return null;
       List<Object> comments = commentRepository.findUserCommentsByPostId(postIds);
       List<Object> likes = likeRepository.findLikesWithCommentIds(postIds);
       List<Object> results = new ArrayList<>();
       results.addAll(likes);
       results.addAll(comments);
       return results;
    }
}
