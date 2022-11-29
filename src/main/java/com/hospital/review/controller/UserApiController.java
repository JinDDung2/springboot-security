package com.hospital.review.controller;

import com.hospital.review.domain.Response;
import com.hospital.review.domain.dto.*;
import com.hospital.review.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        UserDto userDto = userService.join(request);
        return Response.success(new UserJoinResponse(userDto.getUsername(), userDto.getEmail()));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}
