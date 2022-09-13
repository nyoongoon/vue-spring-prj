package com.nyoongoon.fullstackjava.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {
    private static final int MAX_SIZE = 2000;
    @Builder.Default //생성자로 받은 값이 null 일경우 기본값으로 빌드
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;
    /*//@Builder => 여기선 @Builder.Default 적용 안됌 => 클래스레벨에 붙이기
    public PostSearch(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }*/
    public long getOffSet() {// 여기 안에서 바꿀 수 있도록 수정하기.
        return (long) (Math.max(1, page) - 1) * Math.min(size, MAX_SIZE); //post 시 page값 0으로 던지면 에러발생하는 상황 처리
    }
}
