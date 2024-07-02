package org.example.bookstorebackend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserAuth {
    private String username;
    private String password;
    private Long userId;
    private Boolean isAdmin;
    private String email;
}
