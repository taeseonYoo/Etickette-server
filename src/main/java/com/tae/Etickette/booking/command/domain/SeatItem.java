package com.tae.Etickette.booking.command.domain;

import com.tae.Etickette.global.jpa.MoneyConverter;
import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatItem {
    @Embedded
    private BookSeatId seatId;
    @Convert(converter = MoneyConverter.class)
    private Money price;

    public SeatItem(BookSeatId seatId, Money price) {
        this.seatId = seatId;
        this.price = price;
    }
}
