package com.hospital.review.domain.dto;

import com.hospital.review.domain.User;
import lombok.*;

@Builder
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
