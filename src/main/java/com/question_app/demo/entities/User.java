package com.question_app.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Table
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String userName;
    String password;
}
