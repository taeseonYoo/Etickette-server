package com.tae.Etickette.member.dto;

import com.tae.Etickette.member.entity.Member;
import com.tae.Etickette.member.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberJoinRequestDto {
    @Size(max = 20)
    @NotBlank
    private final String name;
    @Email
    @NotBlank
    private final String email;
    @Size(min = 8, max = 20)
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 대소문자, 숫자, 특수문자를 포함해야 합니다")
    private final String password;

    @Builder
    public MemberJoinRequestDto(String name, String email, String password) {
        this.name=name;
        this.email=email;
        this.password = password;
    }

    public Member toEntity(String encodedPassword) {
        return Member.create(name, email, encodedPassword, Role.USER);
    }

}
