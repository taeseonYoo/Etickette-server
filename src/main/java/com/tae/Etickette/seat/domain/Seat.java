package com.tae.Etickette.seat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;
    @Column(name = "seat_row")
    private String row;
    @Column(name = "seat_column")
    private String column;

    private String locX;
    private String locY;

    private Long concertId;
    private Seat(String row, String column, Long concertId) {
        this.row = row;
        this.column = column;
        this.concertId = concertId;
        this.locX="";
        this.locY = "";
    }

    public static Seat create(String row, String column, Long concertId) {
        return new Seat(row, column, concertId);
    }
}
