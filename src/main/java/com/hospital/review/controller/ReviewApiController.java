package com.hospital.review.controller;

import com.hospital.review.domain.dto.ReviewCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;

@RestController
@RequestMapping("/api/v1/reviews")
@Slf4j
public class ReviewApiController {

    @PostMapping()
    public String write(@RequestBody ReviewCreateRequest request, Authentication authentication) {
        log.info("ReviewApiController user={}", authentication.getUsername());
        return "리뷰 등록에 성공";
    }

}
