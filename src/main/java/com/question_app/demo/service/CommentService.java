package com.question_app.demo.service;

import com.question_app.demo.entities.Comment;
import com.question_app.demo.entities.Post;
import com.question_app.demo.entities.User;
import com.question_app.demo.repo.CommentRepository;
import com.question_app.demo.repo.PostRepository;
import com.question_app.demo.repo.UserRepository;
import com.question_app.demo.request.CommentCreateRequest;
import com.question_app.demo.request.CommentUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<Comment> getAll(Optional<Long> userId, Optional<Long> postId) {
        if (postId.isPresent() && userId.isPresent()) {
            return commentRepository.findByPostIdAndUserId(postId.get(), userId.get());
        } else if (postId.isPresent()) {
            return commentRepository.findByPostId(postId.get());
        } else if (userId.isPresent()) {
            return commentRepository.findByUserId(userId.get());
        }
        return commentRepository.findAll();
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment create(CommentCreateRequest commentCreateRequest) {
        Optional<User> user = userRepository.findById(commentCreateRequest.getUserId());
        if (user.isPresent()) {
            Optional<Post> post = postRepository.findById(commentCreateRequest.getPostId());
            if (post.isPresent()) {
                Comment comment = new Comment();
                comment.setId(commentCreateRequest.getId());
                comment.setPost(post.get());
                comment.setText(commentCreateRequest.getComment());
                comment.setUser(user.get());
                return commentRepository.save(comment);
            }
        }
        return null;
    }

    public Comment updateComment(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            Comment _comment = comment.get();
            _comment.setText(commentUpdateRequest.getComment());
            return commentRepository.save(_comment);
        }
        return null;
    }
}
