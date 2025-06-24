package com.tae.Etickette.seat.query;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seat")
@Getter
public class SeatData {
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
}
