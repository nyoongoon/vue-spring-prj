package com.nyoongoon.fullstackjava.repository;

import com.nyoongoon.fullstackjava.domain.Post;
import com.nyoongoon.fullstackjava.domain.QPost;
import com.nyoongoon.fullstackjava.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor //생성자 자동주입
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
                .offset((long)(postSearch.getOffSet()))// 의존성 줄이도록 수정했음
                .orderBy(QPost.post.id.desc())
                .fetch();
    }
}
