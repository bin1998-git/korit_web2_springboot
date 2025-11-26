package com.koreait.spring_boot_study.repository.impl;

import com.koreait.spring_boot_study.entity.Post;
import com.koreait.spring_boot_study.repository.PostRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class PostRepository implements PostRepo {
    // CRUD(생성, 조회, 수정, 삭제)
    // DB 대용 필드 - SQL쿼리로 DB에서 데이터를 받아옴(주로 List로)

    private List<Post> posts = new ArrayList<>(
            Arrays.asList(
                    new Post(1, "페이커 vs 손흥민", "누가 이김?"),
                    new Post(2, "박지성 vs 손흥민", "누가 이김?"),
                    new Post(3, "피카츄 vs 라이츄", "누가 이김?"),
                    new Post(4, "스프링부트 공부중", "반복 ㄱㄱ")
            )
    );

    // 전체게시글 조회
    public List<Post> findAllPostList() {
        return posts;
    }

    // 게시글 단건 조회
    public Optional<Post> findPostById(int id){
        return posts.stream()
                .filter(post -> post.getId() == id)
                .findFirst(); // 객체가 있으면 객체를 optional로 감싸 리턴
        // 없으면 null을 optional로 감싸 리턴

    }

    // 단건 추가

    public int insertPost(String title, String content) {
        // maxId
        int maxId = 0;
        for (Post post : posts) {
            if (post.getId() > maxId) {
                maxId = post.getId();
            }
        } // auto_increment 기능
        Post post = new Post(maxId + 1, title, content);
        posts.add(post); // sql insert 쿼리

        return 1;
    }

    // 단건 삭제 by id
    public int deletePostById(int id) {
        // id가 검증이안되면 return 0
        // optional<> -> 코드를 선언하는 쪽에서 타입을 지정하겠다 : 제네릭
        Optional<Post> taget = posts.stream()
                .filter(post -> post.getId() == id)
                .findFirst();

        if (taget.isEmpty()) {
            return 0;
        }

        Post post = taget.get(); // 옵셔널이 가지고 있는 값 가져오기
        posts.remove(post);
        log.info("상품삭제완료 : {}", post);
        return 1;
    }

    // 단건 업데이트
    public int updatePost(int id, String title, String content) {
        Post taget = null;
        for (Post post : posts) {
            if (post.getId() == id) {
                taget = post;
                break; // findFirst()
            }
        }
        if (taget == null) {
            return 0;
        }
        int index = posts.indexOf(taget); // 기존객체 : target
        // 외부에서 가져온 데이터로 객체를 새로 생성(newPost)
        Post newPost = new Post(id, title, content);
        // index(target이 있던자리)에 새로만든 newPost를 덮어씌워 주세요
        posts.set(index, newPost);
        return 1;
    }
}


