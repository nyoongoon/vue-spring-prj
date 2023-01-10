package com.nyoongoon.fullstackjava.controller;

import com.nyoongoon.fullstackjava.repository.UserRepository;
import com.nyoongoon.fullstackjava.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController { //로그인하여 토큰 발급

    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public void login(@RequestBody Login login){
        //json 아이디/비밀번호
        log.info(">>>login={}", login);

        //DB 조회


        //토큰을 응답
    }
}
