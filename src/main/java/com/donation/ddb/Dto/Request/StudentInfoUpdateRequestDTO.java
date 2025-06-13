package com.donation.ddb.Dto.Request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class StudentInfoUpdateRequestDTO {
    // 닉네임 수정
    //@Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
    @NotBlank(message="닉네임을 입력해주세요")
    private String nickname;

    // 비밀번호 수정 (현재 비밀번호 확인용)
    @NotBlank(message="비밀번호를 입력해주세요")
    private String currentPassword;
}
