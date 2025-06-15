package com.tae.Etickette.member.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteMemberRequest {
    @NotBlank
    String password;
    @Builder
    public DeleteMemberRequest(String password) {
        this.password = password;
    }
}
