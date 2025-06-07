package com.tae.Etickette.session.application.Dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class RegisterSessionReqDto {
    private final Long concertId;
    private final Long venueId;
    private final List<SessionInfo> sessionInfos;

    @Builder
    public RegisterSessionReqDto(Long concertId, Long venueId, List<SessionInfo> sessionInfos) {
        this.concertId = concertId;
        this.venueId = venueId;
        this.sessionInfos = sessionInfos;
    }

    public List<LocalDate> getConcertDates() {
        return sessionInfos.stream().map(SessionInfo::getConcertDate).toList();
    }

    @Getter
    public static class SessionInfo {
        private LocalDate concertDate;
        private LocalTime startTime;

        @Builder
        public SessionInfo(LocalDate concertDate, LocalTime startTime) {
            this.concertDate = concertDate;
            this.startTime = startTime;
        }
    }
}
