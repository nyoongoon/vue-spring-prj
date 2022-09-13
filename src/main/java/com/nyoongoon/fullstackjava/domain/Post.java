package com.nyoongoon.fullstackjava.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access =  AccessLevel.PUBLIC) //파라미터가 없는 기본 생성자를 생성,
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostEditor.PostEditorBuilder toEditor(){
        return PostEditor.builder()
                .title(this.title)
                .content(this.content);
                //.build();   ==>빌더를 넘기는 것이기때문에 빌드 메소드를 사용하면 안됨 !!
    }

    public void edit(PostEditor postEditor) {
        this.title = postEditor.getTitle();
        this.content = postEditor.getContent();
    }
}
