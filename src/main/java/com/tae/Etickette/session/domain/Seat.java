package com.tae.Etickette.session.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {
    private String rowNum;
    private Integer columnNum;
    private String grade;

    public Seat(String rowNum, Integer columnNum) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
    }

    public Seat(String rowNum, Integer columnNum, String grade) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(rowNum, seat.rowNum) && Objects.equals(columnNum, seat.columnNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNum, columnNum);
    }
}
