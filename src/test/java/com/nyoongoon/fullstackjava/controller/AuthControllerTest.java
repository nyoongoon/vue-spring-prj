package com.nyoongoon.fullstackjava.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nyoongoon.fullstackjava.domain.User;
import com.nyoongoon.fullstackjava.repository.PostRepository;
import com.nyoongoon.fullstackjava.repository.UserRepository;
import com.nyoongoon.fullstackjava.request.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc //하면 SrpingBootTest에서도 MockMvc 주입 가능
@SpringBootTest // MockMvc 주입 x
class AuthControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test() throws Exception {
        //given
        userRepository.save(User.builder()
                .name("호돌맨")
                .email("hodolman88@gmail.com")
                .password("1234") // 암호화 알고리즘 -> Scrypt, Bcrypt
                .build());


        Login login = Login.builder()
                .email("hodolman88@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //then
    }
}