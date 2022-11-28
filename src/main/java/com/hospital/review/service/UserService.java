package com.hospital.review.service;

import com.hospital.review.domain.User;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /** 회원가입 */
    public UserDto join(UserJoinRequest request) {
        // 유저네임 중복체크
        // 중복 되어있으면 Exception 처리 발생
        userRepository.findByUsername(request.getUsername())
                .ifPresent(user -> {
                    throw new RuntimeException("username이 중복됩니다.");
                });



        // 회원가입
        User savedUser = userRepository.save(request.toEntity());
        return UserDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();
    }

}
