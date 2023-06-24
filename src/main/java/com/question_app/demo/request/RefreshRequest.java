package com.question_app.demo.request;

import lombok.Data;

@Data
public class RefreshRequest {
    Long userId;
    String refreshToken;
}
