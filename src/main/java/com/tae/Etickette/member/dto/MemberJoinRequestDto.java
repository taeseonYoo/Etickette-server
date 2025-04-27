package com.tae.Etickette.member.dto;

import com.tae.Etickette.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    private final String password;


    @Builder
    public MemberJoinRequestDto(String name, String email, String password) {
        this.name=name;
        this.email=email;
        this.password = password;
    }

    public Member toEntity() {
        return Member.create(name, email, password);
    }
}
