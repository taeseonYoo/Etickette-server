package com.tae.Etickette.concert.command.domain;

import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.global.jpa.MoneyConverter;
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
