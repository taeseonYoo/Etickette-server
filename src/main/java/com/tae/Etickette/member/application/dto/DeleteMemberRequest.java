package com.tae.Etickette.member.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteMemberRequest {
    @NotBlank
    @Schema(description = "삭제할 회원의 이메일",example = "tae@ticket.com")
    private String email;
    @Builder
    public DeleteMemberRequest(String email) {
        this.email = email;
    }
}
