package com.koreait.spring_boot_study.service;

import com.koreait.spring_boot_study.dto.AddPostReqDto;
import com.koreait.spring_boot_study.dto.PostResDto;
import com.koreait.spring_boot_study.entity.Post;
import com.koreait.spring_boot_study.exception.PostNotFoundException;
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
    public List<String> getAllPostNames() {
        return postRepository.findAllPostList() // list<post>
                .stream()
                .map(post -> post.getTitle())
                .collect(Collectors.toList());
    }

    // 게시글 단건조회 : isEmpty -> 정석) 예외를 던져야함(커스텀예외)
    public String getPostTitleById(int id) {
        Optional<Post> postOptional = postRepository.findPostById(id);
       // 옵셔널을 언패킹하는 다른 방법(예외도 같이 던질 수 있음)
        // 옵셔널.orElseThrow() :
        // Optional에 포장된 객체가 null이 아니면 post 변수에 담고,
        // null이면 예외를 던지세요.
        Post post = postOptional.orElseThrow(
                () -> new PostNotFoundException("게시글을 찾을 수 없습니다.")
        );
        String title = post.getTitle();
        return title;
    }

    // 게시글 전체 리턴
    public List<PostResDto> getAllPost() {
        return postRepository.findAllPostList()
                .stream()
                .map(post -> new PostResDto(post.getTitle()
                        ,post.getContent()))
                .collect(Collectors.toList()); // List<PostResDto>

    }

    // 게시글 단건 조회
    public PostResDto getPostById(int id) {
        Post post = postRepository.findPostById(id) // Optional<Post>
                .orElseThrow(
                        () -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
        return new PostResDto(post.getTitle(), post.getContent());
    }


    // 상품추가 등록
    public void addPost(AddPostReqDto dto) {
        int successCount = postRepository
                .insertPost(dto.getTitle(), dto.getContent());

        if (successCount <= 0) {
            throw new PostNotFoundException("게시글 등록중 문제가 생겼습니다");
        }
    }



}
