package com.nyoongoon.fullstackjava.controller;

import com.nyoongoon.fullstackjava.exception.InvalidRequest;
import com.nyoongoon.fullstackjava.request.PostCreate;
import com.nyoongoon.fullstackjava.request.PostEdit;
import com.nyoongoon.fullstackjava.request.PostSearch;
import com.nyoongoon.fullstackjava.response.PostResponse;
import com.nyoongoon.fullstackjava.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // Http Method
    // GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT
    // 글 등록
    // POST Method
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        //Post 성공 -> 200, 201
        // Post 요청 케이스
        // Case1. 저장한 데이터 Entity => response로 그대로 응답하기
        // Case2. 저장한 데이터의 primary_id =>response로 응답하기 =>보통 수신안 id를 글조회 api를 통해서 데이터 수신
        // Case3. 응답 필요없음
        // Bad Case : 서버에서 반드시 어떻게 할건지 픽스하는 것은 좋지 않음. => 유연하게 대처하는 것이 좋음 -> 잘 짜기
        /*if (request.getTitle().contains("바보")){
            throw new InvalidRequest();
        }*/
        request.validate();
        postService.write(request);
    }

    /*  /post -> 글 전체 조회(검색 + 페이징)
     *  /posts/{postId} -> 글 한개만 조회
     */
    @GetMapping("/posts/{postId}")
    //public PostResponse get(@PathVariable(name="postId") Long id){
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    // 조회 API => 여러개의 글을 조회
    // 여러개의 글을 조회 API
    /*@GetMapping("/posts")
    public List<PostResponse> getList(Pageable pageable){
        return postService.getList(pageable);
    }*/
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) { //Pageable 대신 **페이징 설정 커스텀**하기 위해 postSearch 객체 사용
        return postService.getList(postSearch);
    }


    @PatchMapping("/posts/{postId}") //PATCH /posts/{postId}
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request){
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);
    }
}
