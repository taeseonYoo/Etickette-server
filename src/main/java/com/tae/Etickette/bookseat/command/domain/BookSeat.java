package com.tae.Etickette.bookseat.command.domain;

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
@Table(name = "book_seat")
public class BookSeat {

    @Id
    @Column(name = "seat_id")
    private Long seatId;
    @Id
    @Column(name = "session_id")
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
        verifySeatNotLocked();
        this.status = SeatStatus.BOOKED;
    }
    private void verifySeatNotLocked() {
        if (!isLocked()) throw new RuntimeException("");
    }
    private boolean isLocked() {
        return this.status == SeatStatus.LOCKED;
    }
    //결제 전 좌석 잠금
    public void lock() {
        verifySeatAvailable();
        this.status = SeatStatus.LOCKED;
    }
    private void verifySeatAvailable() {
        if (isPreempted()) throw new RuntimeException("선점 불가능");
    }

    //좌석 취소
    public void cancel() {
        verifySeatPreempt();
        this.status = SeatStatus.AVAILABLE;
    }
    private void verifySeatPreempt(){
        if (!isPreempted()) throw new RuntimeException("취소할 수 없음.");
    }

    private boolean isPreempted() {
        return this.status == SeatStatus.BOOKED || this.status == SeatStatus.LOCKED;
    }


}
