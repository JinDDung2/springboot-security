package com.hospital.review.domain.dto;

import com.hospital.review.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {

    private String username;
    private String password;
    private String email;

    public User toEntity(String password) {
        return User.builder()
                .username(this.username)
                .password(password)
                .email(this.email)
                .build();
    }
}
