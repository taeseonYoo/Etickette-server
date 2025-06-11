package com.tae.Etickette.global.model;

import com.tae.Etickette.global.jpa.MoneyConverter;
import jakarta.persistence.Convert;
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
    @Convert(converter = MoneyConverter.class)
    private Money price;

    public Seat(String rowNum, Integer columnNum) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
    }

    public Seat(String rowNum, Integer columnNum, String grade, Money price) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.grade = grade;
        this.price = price;
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
