package com.hospital.review.controller;

import com.hospital.review.domain.dto.ReviewCreateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewApiController {

    @PostMapping()
    public String write(@RequestBody ReviewCreateRequest request) {
        return "리뷰 등록에 성공";
    }

}
