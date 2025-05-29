package com.tae.Etickette.booking;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {
    private String row;
    private Integer column;

    public Seat(String row, Integer column) {
        this.row = row;
        this.column = column;
    }
}
