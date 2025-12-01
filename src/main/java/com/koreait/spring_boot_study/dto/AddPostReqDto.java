package com.koreait.spring_boot_study.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class AddPostReqDto {
    @NotBlank(message = "제목은 비어있을 수 없습니다.")
    @Size(max = 1000, message = "1000 글자를 초과할 수 없습니다")
    private String title;
    @NotBlank(message = "내용은 비어있을 수 없습니다.")
    @Size(max = 1000, message = "1000 글자를 초과할 수 없습니다")
    private String content;
}
