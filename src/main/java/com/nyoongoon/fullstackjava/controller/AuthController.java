package com.nyoongoon.fullstackjava.controller;

import com.nyoongoon.fullstackjava.domain.User;
import com.nyoongoon.fullstackjava.exception.InvalidRequest;
import com.nyoongoon.fullstackjava.exception.InvalidSiginInformation;
import com.nyoongoon.fullstackjava.repository.UserRepository;
import com.nyoongoon.fullstackjava.request.Login;
import com.nyoongoon.fullstackjava.response.SessionResponse;
import com.nyoongoon.fullstackjava.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController { //로그인하여 토큰 발급

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Login login){
        //json 아이디/비밀번호
        log.info(">>>login={}", login);
        String accessToken = authService.sigin(login);
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();

        log.info(">>>>>>>>>>>>>>> cookie={}", cookie.toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();

    }
}
