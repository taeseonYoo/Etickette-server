package com.tae.Etickette.bookseat.domain;

import com.tae.Etickette.global.jpa.MoneyConverter;
import com.tae.Etickette.global.model.Money;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(BookSeatId.class)
public class BookSeat {

    @Id
    private Long seatId;
    @Id
    private Long sessionId;

    private String grade;

    @Convert(converter = MoneyConverter.class)
    private Money price;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    private BookSeat(Long seatId, Long sessionId, String grade, Money price) {
        this.seatId = seatId;
        this.sessionId = sessionId;
        this.grade = grade;
        this.price = price;
        this.status = SeatStatus.AVAILABLE;
    }

    public static BookSeat create(Long seatId, Long sessionId, String grade, Money price) {
        return new BookSeat(seatId, sessionId, grade, price);
    }


    public void reserve() {
        this.status = SeatStatus.BOOKED;
    }
    public void lock() {
        verifySeatNotBooked();
        this.status = SeatStatus.LOCKED;
    }
    public void cancel() {
        verifySeatNotBooked();
        this.status = SeatStatus.AVAILABLE;
    }
    private void verifySeatNotBooked(){
        if (!isNotYetBooked()) throw new RuntimeException("변경 할 수 없음");
    }

    private boolean isNotYetBooked() {
        return this.status == SeatStatus.AVAILABLE || this.status == SeatStatus.LOCKED;
    }

}
