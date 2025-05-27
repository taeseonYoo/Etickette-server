package com.tae.Etickette.concert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade {
    private String grade;
    private Integer price;

    public Grade(String grade, Integer price) {
        this.grade = grade;
        this.price = price;
    }
}
