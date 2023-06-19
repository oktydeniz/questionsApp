package com.question_app.demo.response;


import com.question_app.demo.entities.Comment;
import lombok.Data;

@Data
public class CommentResponse {

    Long id;
    Long userId;
    String text;
    String userName;

    public CommentResponse(Comment comment){
        this.id = comment.getId();
        this.text = comment.getText();
        this.userName = comment.getUser().getUserName();
        this.userId = comment.getUser().getId();
    }
}
