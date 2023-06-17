package com.question_app.demo.service;

import com.question_app.demo.entities.Post;
import com.question_app.demo.entities.User;
import com.question_app.demo.repo.PostRepository;
import com.question_app.demo.request.PostCreateRequest;
import com.question_app.demo.request.PostUpdateRequest;
import com.question_app.demo.response.LikeResponse;
import com.question_app.demo.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private PostRepository postRepository;
    private UserService userService;

    private LikeService likeService;

    public PostService(PostRepository postRepository, UserService userService, @Lazy LikeService likeService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.likeService = likeService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> list;
        if (userId.isPresent()) {
            list = postRepository.findByUserId(userId.get());
        } else list = postRepository.findAll();
        return list.stream().map((p) -> {
            List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));
            return new PostResponse(p, likes);
        }).collect(Collectors.toList());
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public PostResponse getPostByIdWithLikes(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));
        return new PostResponse(post, likes);

    }

    public Post createPost(PostCreateRequest p) {
        User user = userService.findUser(p.getUserId());
        if (user == null) {
            return null;
        }
        Post post = new Post();
        post.setText(p.getText());
        post.setTitle(p.getTitle());
        post.setId(p.getId());
        post.setUser(user);
        post.setCreateDate(new Date());
        return postRepository.save(post);
    }

    public Post updatePostById(Long postId, PostUpdateRequest postUpdateRequest) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            Post _p = post.get();
            _p.setTitle(postUpdateRequest.getTitle());
            _p.setText(postUpdateRequest.getText());
            return postRepository.save(_p);
        }
        return null;
    }

    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
    }
}
