package com.tae.Etickette.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {
    @NotBlank
    String oldPassword;
    @Size(min = 8,max = 20)
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 대소문자, 숫자, 특수문자를 포함해야 합니다")
    String newPassword;
    String email;

    @Builder
    public PasswordUpdateRequestDto(String oldPassword, String newPassword,String email) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.email = email;
    }
}
