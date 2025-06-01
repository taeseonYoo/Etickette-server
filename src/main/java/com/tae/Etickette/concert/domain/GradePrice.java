package com.tae.Etickette.concert.domain;

import com.tae.Etickette.booking.Money;
import com.tae.Etickette.booking.MoneyConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GradePrice {
    private String grade;
    @Convert(converter = MoneyConverter.class)
    private Money price;

    public GradePrice(String grade, Money price) {
        this.grade = grade;
        this.price = price;
    }
}
