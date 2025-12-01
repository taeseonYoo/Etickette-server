package com.tae.Etickette.member.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterMemberResponse {
    @Schema(description = "회원 ID",example = "1")
    private final Long id;
    @Schema(description = "회원 이름",example = "tae")
    private final String name;
    @Schema(description = "회원 이메일",example = "tae@ticket.com")
    private final String email;

    @Builder
    public RegisterMemberResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
