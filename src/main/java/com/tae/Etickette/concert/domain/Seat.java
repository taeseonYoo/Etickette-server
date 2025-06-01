package com.tae.Etickette.concert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    private String rowNum;
    private Integer columnNum;
    private String grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    private Seat(String row, Integer column, String grade, Venue venue) {
        this.rowNum = row;
        this.columnNum = column;
        this.grade = grade;
        this.venue = venue;
    }

    public static Seat create(String row, Integer column, String grade, Venue venue) {
        return new Seat(row, column, grade, venue);
    }

}
