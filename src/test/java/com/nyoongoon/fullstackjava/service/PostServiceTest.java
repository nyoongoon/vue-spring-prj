package com.nyoongoon.fullstackjava.service;

import com.nyoongoon.fullstackjava.domain.Post;
import com.nyoongoon.fullstackjava.exception.PostNotFound;
import com.nyoongoon.fullstackjava.repository.PostRepository;
import com.nyoongoon.fullstackjava.request.PostCreate;
import com.nyoongoon.fullstackjava.request.PostEdit;
import com.nyoongoon.fullstackjava.request.PostSearch;
import com.nyoongoon.fullstackjava.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        //when
        postService.write(postCreate);

        //then
        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);
        //when
        PostResponse response = postService.get(requestPost.getId());
        //then
        Assertions.assertNotNull(response);
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회 - QueryDsl 사용")
    void test3_1() {
        List<Post> requestPosts = IntStream.range(0, 20)  // request를 먼저 엔티티로 변화를 해서 저장.
                .mapToObj(i -> {
                    return Post.builder()
                            .title("foo" + i)
                            .content("bar" + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        //Pageable pageable = PageRequest.of(0, 5); //여기는 고정이라 0으로 삽입
        //when
        //List<PostResponse> posts = postService.getList(pageable); // Pageable 대신 PostSearch 사용
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();
        List<PostResponse> posts = postService.getList(postSearch); // Pageable 대신 PostSearch 사용
        //then
        assertEquals(10L, posts.size());
        assertEquals("foo19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        List<Post> requestPosts = IntStream.range(0, 31)  // request를 먼저 엔티티로 변화를 해서 저장.
                .mapToObj(i -> {
                    return Post.builder()
                            .title("제목 " + i)
                            .content("내용 " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        //Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id"); //여기는 고정이라 0으로 삽입
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        //when
        List<PostResponse> posts = postService.getList(postSearch); // Pageable 대신 PostSearch 사용

        //then
        assertEquals(10L, posts.size());
        assertEquals("제목 30", posts.get(0).getTitle());
        assertEquals("제목 26", posts.get(4).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        //given
        Post post = Post.builder().title("제목 1")
                .content("내용 1")
                .build();
        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder()
                        .title("타이틀 1")
                        .build();
        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(()-> new RuntimeException("글이 존재하지 않습니다. id = "+ post.getId()));
        Assertions.assertEquals("타이틀 1", changedPost.getTitle());
        Assertions.assertEquals("내용 1", changedPost.getContent()); // 내용 수정에 안넣었을경우 ?
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        //given
        Post post = Post.builder().title("제목 1")
                .content("내용 1")
                .build();
        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder()
                .content("콘텐츠 1")
                .build();
        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(()-> new RuntimeException("글이 존재하지 않습니다. id = "+ post.getId()));
        Assertions.assertEquals("제목 1", changedPost.getTitle());
        Assertions.assertEquals("콘텐츠 1", changedPost.getContent()); // 내용 수정에 안넣었을경우 ?
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        //given
        Post post = Post.builder().title("제목 1")
                .content("내용 1")
                .build();
        postRepository.save(post);
        //when
        postService.delete(post.getId());
        //then
        Assertions.assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("글 1 개 조회 - 존재하지 않는 글")
    void test7() {
        //given
        Post post = Post.builder().title("제목 1")
                .content("내용 1")
                .build();
        postRepository.save(post);
        //expected
       Assertions.assertThrows(PostNotFound.class, () -> {// 어떤 예외가 발생했는가를 인자로 받음
            postService.get(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test8() {
        //given
        Post post = Post.builder().title("제목 1")
                .content("내용 1")
                .build();
        postRepository.save(post);
        //expected
        Assertions.assertThrows(PostNotFound.class, () -> {// 어떤 예외가 발생했는가를 인자로 받음
            postService.delete(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test9() {
        //given
        Post post = Post.builder().title("제목 1")
                .content("내용 1")
                .build();
        postRepository.save(post);
        PostEdit postEdit = PostEdit.builder()
                .content("콘텐츠 1")
                .build();
        //when
        //expected
        Assertions.assertThrows(PostNotFound.class, () -> {// 어떤 예외가 발생했는가를 인자로 받음
            postService.edit(post.getId() + 1L, postEdit);
        });
    }
}