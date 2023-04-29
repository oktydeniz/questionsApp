package com.question_app.demo.request;

import lombok.Data;

@Data
public class CommentCreateRequest {
    Long id;
    String comment;
    Long userId;
    Long postId;
}
