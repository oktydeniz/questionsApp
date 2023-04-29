package com.question_app.demo.response;

import com.question_app.demo.entities.Like;
import com.question_app.demo.entities.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostResponse {

    Long id;
    Long userId;
    String userName;
    String title;
    String text;

    List<LikeResponse> likes;

    public PostResponse(Post post, List<LikeResponse> likes) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.userName = post.getUser().getUserName();
        this.text = post.getText();
        this.title = post.getTitle();
        this.likes = likes;
    }
}
