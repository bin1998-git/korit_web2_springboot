package com.koreait.spring_boot_study.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class AddPostReqDto {
    private String title;
    private String content;
}
