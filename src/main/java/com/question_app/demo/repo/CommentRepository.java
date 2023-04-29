package com.question_app.demo.repo;

import com.question_app.demo.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    List<Comment> findByUserId(Long userId);

    List<Comment> findByPostIdAndUserId(Long aLong, Long aLong1);
}
