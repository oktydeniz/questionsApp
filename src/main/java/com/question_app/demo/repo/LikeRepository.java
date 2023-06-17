package com.question_app.demo.repo;

import com.question_app.demo.entities.Comment;
import com.question_app.demo.entities.Like;
import com.question_app.demo.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {


    List<Like> findByUserIdAndPostId(Long userId, Long postId);

    List<Like> findByUserId(Long userId);

    List<Like> findByPostId(Long postId);

    @Query(value = "select 'liked', l.post_id, u.avatar, u.user_name from " +
            "p_like l left join user u on u.id = l.user_id " +
            "where l.post_id in :postId limit 5", nativeQuery = true)
    List<Object> findLikesWithCommentIds(@Param("postId") List<Long> postIds);
}
