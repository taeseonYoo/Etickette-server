package com.tae.Etickette.member.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteMemberRequest {
    @NotBlank
    private String email;
    @Builder
    public DeleteMemberRequest(String email) {
        this.email = email;
    }
}
