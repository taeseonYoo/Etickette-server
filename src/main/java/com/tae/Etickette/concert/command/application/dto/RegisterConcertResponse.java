package com.tae.Etickette.concert.command.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterConcertResponse {
    private Long concertId;

    @Builder
    public RegisterConcertResponse(Long concertId) {
        this.concertId = concertId;
    }
}
