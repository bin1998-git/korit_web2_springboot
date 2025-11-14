package com.koreait.spring_boot_study.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString // JSON 작업시 필요
@Getter   // JSON 작업시 필요
@Setter    // JSON 작업시 필요
public class StudyReqDto {
    private String data1;
    private String data2;

}
