package com.koreait.spring_boot_study.service;

import com.koreait.spring_boot_study.entity.Post;
import com.koreait.spring_boot_study.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 전체 조회
    public List<String> allListName() {
      return postRepository.allList() // list<post>
              .stream()
              .map(post -> post.getTitle())
              .collect(Collectors.toList());
    }

    // 게시글 단건조회 : isEmpty -> 정석) 예외를 던져야함




}
