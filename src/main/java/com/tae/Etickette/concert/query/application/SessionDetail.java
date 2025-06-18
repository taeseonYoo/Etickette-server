package com.tae.Etickette.concert.query.application;

import com.tae.Etickette.session.domain.SessionStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class SessionDetail {
    private Long sessionId;
    private LocalDate concertDate;
    private LocalDate startTime;

    private SessionStatus status;
}
