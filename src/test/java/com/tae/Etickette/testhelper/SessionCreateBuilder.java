package com.tae.Etickette.testhelper;

import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.tae.Etickette.session.application.Dto.RegisterSessionRequest.*;

public class SessionCreateBuilder {
    private Long concertId;
    private Long venueId;
    private List<SessionInfo> sessionInfos = List.of(new SessionInfo(LocalDate.of(2025, 6, 1), LocalTime.of(15, 0)));


    public static SessionCreateBuilder builder() {
        return new SessionCreateBuilder();
    }
    public RegisterSessionRequest build() {
        return RegisterSessionRequest.builder()
                .concertId(concertId)
                .venueId(venueId)
                .sessionInfos(sessionInfos)
                .build();
    }

    public SessionCreateBuilder concertId(Long concertId) {
        this.concertId = concertId;
        return this;
    }
    public SessionCreateBuilder venueId(Long venueId) {
        this.venueId = venueId;
        return this;
    }
    public SessionCreateBuilder sessionInfos(List<SessionInfo> sessionInfos) {
        this.sessionInfos = sessionInfos;
        return this;
    }


}
