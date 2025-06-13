package com.donation.ddb.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class StudentInfoUpdatePwdDTO {
    // 비밀번호 수정 (현재 비밀번호 확인용)
    @NotBlank(message = "현재 비밀번호를 입력해주세요")
    private String currentPassword;

    // 새 비밀번호
    @NotBlank(message = "새 비밀번호를 입력해주세요")
    private String newPassword;

    // 새 비밀번호 확인
    @NotBlank(message = "비밀번호 확인을 입력해주세요")
    private String confirmNewPassword;
}
