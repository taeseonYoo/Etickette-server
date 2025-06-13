package com.tae.Etickette.session.domain;

import com.tae.Etickette.global.event.Events;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Column
    private LocalDate concertDate;
    @Column
    private LocalTime startTime;
    @Column
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;
    /**
     * 연관관계
     */
    @Column
    private Long concertId;
    @Column
    private Long venueId;


    private Session(LocalDate concertDate, LocalTime startTime, Integer runningTime, Long concertId, Long venueId) {
        this.concertDate = concertDate;
        this.startTime = startTime;
        calculateEndTime(startTime, runningTime);
        this.status = SessionStatus.BEFORE;
        this.concertId = concertId;
        this.venueId = venueId;
    }

    public static Session create(LocalDate concertDate, LocalTime startTime, Integer runningTime, Long concertId,Long venueId) {
        return new Session(concertDate, startTime, runningTime, concertId, venueId);
    }

    //공연 종료시간을 계산한다.
    private void calculateEndTime(LocalTime startTime, Integer runningTime) {
        this.endTime = startTime.plusMinutes(runningTime);
    }


    public boolean isActive() {
        return this.status == SessionStatus.BEFORE || this.status == SessionStatus.OPEN;
    }

    public void openSchedule() {
        this.status = SessionStatus.OPEN;
    }
    public void cancel() {
        verifyNotYetStarted();
        this.status = SessionStatus.CANCELED;
        Events.raise(new SessionCanceledEvent(this.getId()));
    }

    private void verifyNotYetStarted() {
        if (!isNotYetStarted())
            throw new AlreadyStartedException("이미 시작 된 공연입니다.");
    }

    private boolean isNotYetStarted() {
        return status ==  SessionStatus.BEFORE || status ==  SessionStatus.OPEN || status ==  SessionStatus.COMPLETED;
    }


}
