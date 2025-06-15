package com.tae.Etickette.member.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterMemberResponse {

    private final Long id;
    private final String name;
    private final String email;

    @Builder
    public RegisterMemberResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
