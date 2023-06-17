package com.question_app.demo.controller;

import com.question_app.demo.entities.Post;
import com.question_app.demo.request.PostCreateRequest;
import com.question_app.demo.request.PostUpdateRequest;
import com.question_app.demo.response.PostResponse;
import com.question_app.demo.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostResponse> getPosts(@RequestParam Optional<Long> userId) {
        return postService.getAllPosts(userId);
    }

    @GetMapping("/{postId}")
    public PostResponse getPost(@PathVariable Long postId) {
        return postService.getPostByIdWithLikes(postId);
    }

    @PostMapping
    public Post createPost(@RequestBody PostCreateRequest postCreateRequest) {
        return postService.createPost(postCreateRequest);
    }

    @PutMapping("/{postId}")
    public Post updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        return postService.updatePostById(postId, postUpdateRequest);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePostById(postId);
    }
}
