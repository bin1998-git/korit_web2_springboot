package com.koreait.spring_boot_study.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModifyPostReqDto {
    @NotBlank(message = "제목은 비울수 없습니다.")
    @Size(max = 50, message = "제목은 50자 이상 불가능합니다.")
    private String title;

    @NotBlank(message = "내용은 비울수 없습니다.")
    @Size(max = 200, message = "내용은 200자 이상 작성할 수 없습니다.")
    private String content;
}
