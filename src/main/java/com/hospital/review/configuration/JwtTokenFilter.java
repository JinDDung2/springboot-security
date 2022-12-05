package com.hospital.review.configuration;

import com.hospital.review.domain.User;
import com.hospital.review.service.UserService;
import com.hospital.review.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 권한에 따른 허락, 거절
        // 현재 모두 닫혀있는 상태

        // 거절해야하는 경우
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorizationHeader={}", authorizationHeader);

        // 1. request 할 때 토큰이 없는 경우
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.error("헤더를 가져오는 과정에서 에러가 났습니다. 헤더가 null 이거나 잘못되었습니다.");
            filterChain.doFilter(request,response);
            return;
        }

        String token;
        try {
            token = authorizationHeader.split(" ")[1].trim();
        }catch (Exception e) {
            log.error("token 추출에 실패했습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        if (JwtTokenUtil.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 권한확인 작업에 따른 허락

        // 토큰에서 claim 꺼내고 claim 에서 username 꺼내기
        String username = JwtTokenUtil.getUsername(token, secretKey);
        log.info("username={}", username);

        // userDetails
        User user = userService.findByUsername(username);
        log.info("userRole={}", user.getUserRole());

        // 권한확인 작업에 따른 허락
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), null, List.of(new SimpleGrantedAuthority(user.getUserRole().name())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 권한 부여
        filterChain.doFilter(request, response);

    }
}
