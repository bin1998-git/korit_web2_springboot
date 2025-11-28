package com.koreait.spring_boot_study.entity;

public class Comment {
    private int commentId;
    private String content;

    private Post post; // FK대신에 객체를 필드로 가지고 있어야 한다.
    // comment.getPost().getId() -> 계속된 참조로 탐색하는것(객체 그래프탐색)

    // 하나의 글에 여러개의 댓글이 있다.
    // -> 1개의 Post는 여러개의 Comment 가질수 있다
    // 1. fk 누가 가지고 있어야하나? Comment가 PostId(FK)가지고 있어야 한다.
    // 2. 하나의 댓글은 하나의 글에만 달릴수 있다.
    // -> Post : Comment = 1:N
}
