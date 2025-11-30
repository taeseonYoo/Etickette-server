package com.tae.Etickette.session.application.Dto;

import lombok.Getter;

@Getter
public class RegisterSessionResponse {
    private final Long concertId;

    public RegisterSessionResponse(Long concertId) {
        this.concertId = concertId;
    }
}
