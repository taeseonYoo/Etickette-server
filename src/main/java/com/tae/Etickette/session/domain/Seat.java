package com.tae.Etickette.session.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {
    private String rowNum;
    private Integer columnNum;

    public Seat(String rowNum, Integer columnNum) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
    }
}
