package com.question_app.demo.service;

import com.question_app.demo.entities.Like;
import com.question_app.demo.entities.Post;
import com.question_app.demo.entities.User;
import com.question_app.demo.repo.LikeRepository;
import com.question_app.demo.request.LikeCreateRequest;
import com.question_app.demo.response.LikeResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {
    private LikeRepository likeRepository;
    private UserService userService;
    private PostService postService;

    public LikeService(LikeRepository likeRepository, UserService userService, PostService postService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
        List<Like> list;
        if (userId.isPresent() && postId.isPresent()) {
            list = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
        } else if (userId.isPresent()) {
            list = likeRepository.findByUserId(userId.get());
        } else if (postId.isPresent()) {
            list = likeRepository.findByPostId(postId.get());
        } else list = likeRepository.findAll();
        return list.stream().map(LikeResponse::new).collect(Collectors.toList());
    }


    public Like getOneLikeById(Long LikeId) {
        return likeRepository.findById(LikeId).orElse(null);
    }

    public Like createOneLike(LikeCreateRequest request) {
        User user = userService.findUser(request.getUserId());
        Post post = postService.getPostById(request.getPostId());
        if (user != null && post != null) {
            Like likeToSave = new Like();
            likeToSave.setId(request.getId());
            likeToSave.setPost(post);
            likeToSave.setUser(user);
            return likeRepository.save(likeToSave);
        } else return null;
    }

    public void deleteOneLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }

}
