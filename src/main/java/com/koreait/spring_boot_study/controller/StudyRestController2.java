package com.koreait.spring_boot_study.controller;

import com.koreait.spring_boot_study.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/practice")
// 스프링 부트는 톰캣(자바언어로 만든 서버)을 내장
// 로컬에서 8080포트로 실행된다
// localhost:8080 -> 스프링부트주소
// localhost:8080/practice/practice1 -> practice1메서드 실행
// localhost:8080/practice/practice1?num1=10&num2=20
public class StudyRestController2 {

    //실습) 컨트롤러 2개를 만들어주세요.
    // 1. 쿼리스트링으로 숫자 2개를 받아서 더한 결과를 응답하는 컨트롤러
    @GetMapping("/practice1")
    public int practice1(int num1, int num2) {
        log.info("practice1 컨트롤러 수신");
        int sum = num1 + num2;
        return sum;

    }

    // 2. 쿼리스트링으로 숫자 4개를 받아서 평균을 응답하는 컨트롤러
    // localhost:8080/practice/practice2?num1=10&num2=20&num3=30&num4=40
    @GetMapping("/practice2")
    public double practice2(int num1, int num2, int num3, int num4) {
        log.info("practice2 컨트롤러 수신");
        // 자바복습) double 크키가 int보다 크니까
        // 서로 연산하면 자동으로 double로 변환
        return (num1 + num2 + num3 + num4) / 4.0;
    }

    // 실습3)
    /*
    포스트맨 응답으로
    [
        {
            "name": "홍길동",
            "address": "부산시 연제구"
         },
         {
            "name": "고길동",
            "address": " 부산시 부산진구"
            }
    ]
     */

    @GetMapping("/profiles")
    // localhost:8080/practice/profiles
    public List<?> practice3() {
        log.info("practice3 수신");
        List<Map<String, String>> data = new ArrayList<>();

        Map<String, String> data1 = Map.of(
                "name", "홍길동",
                "address", "부산시 연제구"

        );
        Map<String, String> data2 = Map.of(
                "name", "고길동",
                "address", "부산시 부산진구"

        );

        data.add(data1);
        data.add(data2);
        return data;

    }


    @GetMapping("/profiles2")
    // localhost:8080/practice/profiles2
    public List<?> practice4() {
        log.info("practice4 수신");
        return List.of(
                Map.of(
                        "name", "홍길동",
                        "address", "부산시 연제구"
                ),
                Map.of(
                        "name", "고길동",
                        "address", "부산시 부산진구"
                )
        );
    }

    // 게시물 조회 컨트롤러를 완성해 주세요
    // id로 조회할 수 있게끔 구현해주세요.
    @GetMapping("/lol/{id}")
    public Map<String, Object> getPost(@PathVariable("id") int id) {
        log.info("게시물을 조회합니다");
        List<Post> postList = List.of(
                new Post(1, "페이커 그는 신인가", "ㅇㅈ"),
                new Post(2, "구마유시 그는 신인가", "ㅇㅈ"),
                new Post(3, "케리아 그는 신인가", "ㅇㅈ"),
                new Post(4, "오너 그는 신인가", "ㅇㅈ"),
                new Post(4, "도란 그는 신인가", "ㅇㅈ")
        );

//        Post taget = null;
//        for (Post post : postList) {
//            if (post.getId() == id) {
//                taget = post;
//            }
//        }
//        if (taget == null) {
//            return Map.of("error","해당 id를 가진 게시물이 존재하지 않습니다");
//        }
//        return Map.of("success", taget);


        // stream으로 찾아보자
        // Optional -> null일수도 있는 값을 포장한 컨테이너클래스
        Optional<Post> optionalPost = postList.stream() // List -> stream
                .filter(post -> post.getId() == id) // id가 같은것 빼고 다 삭제
                .findFirst(); // 처음 찾은것을 가져오세요. 타입(옵셔널)

        if (optionalPost.isEmpty()) { // 옵셔널이 가지고 있는 값이 null이라면
            return Map.of("error", "해당 id의 게시글은 존재하지 않습니다.");
        }
        Post taget = optionalPost.get(); // 옵셔널에 포장된 실제값 꺼내기
        return Map.of("success", taget);
    }

}
