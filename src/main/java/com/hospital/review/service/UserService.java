package com.hospital.review.service;

import com.hospital.review.domain.User;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.exception.ErrorCode;
import com.hospital.review.exception.HospitalReviewException;
import com.hospital.review.repository.UserRepository;
import com.hospital.review.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTimeMs = 1000 * 60 * 60;

    public UserDto join(UserJoinRequest request) {
        // 유저네임 중복체크
        // 중복 되어있으면 Exception 처리 발생
        userRepository.findByUsername(request.getUsername())
                .ifPresent(user -> {
                    throw new HospitalReviewException(ErrorCode.DUPLICATED_USER_NAME, String.format("username:%s", request.getUsername()));
                });



        // 회원가입
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return UserDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();
    }

    public String login(String username, String password) {

        // username 일치 확인
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new HospitalReviewException(ErrorCode.NOT_FOUND, String.format("%s 는 가입된 적이 없습니다.", username)));
        // password 일치 확인
        if (!encoder.matches(password, user.getPassword())) {
            throw new HospitalReviewException(ErrorCode.INVALID_PASSWORD, String.format("id 또는 password을 잘못입력 하셨습니다."));
        }

        // 두가지 확인 중 에러 없다면 토큰 발행
        return JwtTokenUtil.createToken(username, secretKey, expireTimeMs);
    }
}
