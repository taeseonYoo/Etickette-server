package com.tae.Etickette.member.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    @NotBlank
    @Schema(description = "이전 비밀번호", example = "@Old1234")
    String oldPassword;
    @Size(min = 8, max = 20)
    @NotBlank
    @Schema(description = "새로운 비밀번호", example = "@New1234")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 대소문자, 숫자, 특수문자를 포함해야 합니다")
    String newPassword;
    @NotBlank
    @Schema(description = "변경할 회원의 이메일", example = "tae@ticket.com")
    String email;

    @Builder
    public ChangePasswordRequest(String oldPassword, String newPassword, String email) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.email = email;
    }
}
