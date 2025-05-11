package com.tae.Etickette.member.service;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDeleteRequestDto {
    @NotBlank
    String password;
    @Builder
    public MemberDeleteRequestDto(String password) {
        this.password = password;
    }
}
