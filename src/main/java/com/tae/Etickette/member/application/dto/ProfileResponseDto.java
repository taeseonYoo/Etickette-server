package com.tae.Etickette.member.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String name;
    private String email;

    @Builder
    public ProfileResponseDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
