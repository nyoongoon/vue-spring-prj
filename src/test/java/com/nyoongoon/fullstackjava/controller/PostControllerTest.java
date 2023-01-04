package com.nyoongoon.fullstackjava.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nyoongoon.fullstackjava.domain.Post;
import com.nyoongoon.fullstackjava.repository.PostRepository;
import com.nyoongoon.fullstackjava.request.PostCreate;
import com.nyoongoon.fullstackjava.request.PostEdit;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


// @WebMvcTest => 애플리케이션의 전반적인 테스트를 하기 때문에(repository, service 레이어...) 이것만으로는 안됨.
@AutoConfigureMockMvc //하면 SrpingBootTest에서도 MockMvc 주입 가능
@SpringBootTest // MockMvc 주입 x
class PostControllerTest {

    @Autowired // 테스트인 경우 필드 자동주입 ㄱㅊㄱㅊ
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }



    @Test
    @DisplayName("글 작성 시 요청시 title값은 필수다.")
    void test2() throws Exception { // 타이틀이 null로 가는 케이스 => 빌더에서 타이틀 메소드 생략
        //given
        PostCreate request = PostCreate.builder() //생성자 입력순서 헷갈리는 위험 없애줌
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("글 작성 시 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        //given
        PostCreate request = PostCreate.builder() //생성자 입력순서 헷갈리는 위험 없애줌
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request); // json으로 변한됨.

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/posts")
                        .header("authorization", "hodolman")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        // then => 디비 저장되어야함.
        assertEquals(1L, postRepository.count());// 전체 테스트하면 실패 -> 맨 위에서 이미 insert 함

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);
        //expected (when_then)   // Mock Mvc
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("1234567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("bar"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)  // request를 먼저 엔티티로 변화를 해서 저장.
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //expected (when_then)   // Mock Mvc
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=0&size=10") //
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", is(10)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", is(30)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title", is("제목 19")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content", is("내용 19")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목 1")
                .content("내용 1")
                .build();
        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder()
                .title("타이틀 1")
                .content("콘텐츠 1")
                .build();
        //expected (when_then)   //수정인 경우 http patch !!!
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId()) //PATCH /posts/{postId}
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목 1")
                .content("내용 1")
                .build();
        postRepository.save(post);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 1개 조회")
    void test9() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);
        //expected (when_then)   // Mock Mvc
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId() + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제")
    void test10() throws Exception {
        //expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test11() throws Exception {
        //given
        PostEdit postEdit = PostEdit.builder()
                .title("타이틀 1")
                .content("콘텐츠 1")
                .build();
        //expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))

                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 작성 시 제목에 '바보'는 포함될 수 없다.")
    void test12() throws Exception {
        //given
        PostCreate request = PostCreate.builder() //생성자 입력순서 헷갈리는 위험 없애줌
                .title("나는 바보입니다.")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request); // json으로 변한됨.

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }



}