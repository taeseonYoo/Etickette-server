package com.tae.Etickette.member.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponse {
    private String name;
    private String email;

    @Builder
    public ProfileResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
