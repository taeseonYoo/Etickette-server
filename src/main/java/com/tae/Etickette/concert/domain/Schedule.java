package com.tae.Etickette.concert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;
    @Column
    private LocalDate concertDate;
    @Column
    private LocalTime concertTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;
    private Schedule(LocalDate concertDate, LocalTime concertTime) {
        this.concertDate = concertDate;
        this.concertTime = concertTime;
    }

    public static Schedule create(LocalDate concertDate, LocalTime concertTime) {
        return new Schedule(concertDate, concertTime);
    }

    public void addConcert(Concert concert) {
        this.concert = concert;
    }
}
