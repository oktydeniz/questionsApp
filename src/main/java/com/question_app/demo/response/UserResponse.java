package com.question_app.demo.response;

import com.question_app.demo.entities.User;
import lombok.Data;

@Data
public class UserResponse {

    Long id;
    int avatar;
    String userName;

    public UserResponse(User user) {
        this.userName = user.getUserName();
        this.avatar = user.getAvatar();
        this.id = user.getId();
    }
}
