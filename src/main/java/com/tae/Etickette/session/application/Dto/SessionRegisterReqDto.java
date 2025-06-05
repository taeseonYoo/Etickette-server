package com.tae.Etickette.session.application.Dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class SessionRegisterReqDto {
    private final LocalDate concertDate;
    private final LocalTime startTime;
    private final Integer runningTime;
    private final Long concertId;
    private final Long venueId;
//    List<Seat> seatingPlan;

    @Builder
    public SessionRegisterReqDto(LocalDate concertDate, LocalTime startTime, Integer runningTime, Long concertId, Long venueId) {
        this.concertDate = concertDate;
        this.startTime = startTime;
        this.runningTime = runningTime;
        this.concertId = concertId;
        this.venueId = venueId;
    }
}
