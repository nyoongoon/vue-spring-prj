package com.nyoongoon.fullstackjava.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nyoongoon.fullstackjava.domain.Post;
import com.nyoongoon.fullstackjava.repository.PostRepository;
import com.nyoongoon.fullstackjava.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.nyoongoon.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;

   /* @BeforeEach ==> mockMvc ?????? ???????????? ??????. ???????????? @AutoConfigureMockMvc
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext) // ?????? ??????
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }*/

    @Test
    @DisplayName("??? ?????? ?????? ?????????")
    void test1() throws Exception {
        //given
        Post post = Post.builder()
                .title("??????")
                .content("??????")
                .build();
        postRepository.save(post);
        //expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry",
                        RequestDocumentation.pathParameters(
                            RequestDocumentation.parameterWithName("postId").description("????????? ID")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("id").description("????????? ID"),
                                PayloadDocumentation.fieldWithPath("title").description("??????"),
                                PayloadDocumentation.fieldWithPath("content").description("??????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ??????")
    void test2() throws Exception {
        //given
        PostCreate request = PostCreate.builder() //????????? ???????????? ???????????? ?????? ?????????
                .title("???????????????.")
                .content("???????????????.")
                .build();
        String json = objectMapper.writeValueAsString(request);
        //expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        //.accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                       PayloadDocumentation.requestFields(
                               PayloadDocumentation.fieldWithPath("title").description("??????").attributes(Attributes.key("constraint").value("?????? ?????? ??????????????????.")),
                               PayloadDocumentation.fieldWithPath("content").description("??????").optional()
                       )
                ));
    }
}
