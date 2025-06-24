package com.tae.Etickette.booking.command.domain;

import com.tae.Etickette.bookseat.domain.BookSeatId;
import com.tae.Etickette.global.event.Events;
import com.tae.Etickette.global.jpa.MoneyConverter;
import com.tae.Etickette.global.model.Money;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {

    //예매 번호
    @EmbeddedId
    private BookingRef bookingRef;

    private Long memberId;
    private Long sessionId;
    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;

    @Convert(converter = MoneyConverter.class)
    private Money totalAmounts;

    private Long paymentId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "line_item",joinColumns = @JoinColumn(name = "booking_id"))
    private List<LineItem> lineItems;

    public Booking(Long memberId, Long sessionId, List<LineItem> lineItems) {
        this.bookingRef = BookingRef.generate();
        this.memberId = memberId;
        this.sessionId = sessionId;
        setLineItems(lineItems);

        status = BookingStatus.BEFORE_PAY;
    }

    private void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
        calculateTotalAmounts();
    }

    private void calculateTotalAmounts() {
        this.totalAmounts = new Money(lineItems.stream().mapToInt(x -> x.getPrice().getValue()).sum());
    }

    public static Booking create(Long memberId, Long sessionId,List<LineItem> lineItems) {
        return new Booking(memberId, sessionId,lineItems);
    }

    public void cancel() {
        verifyNotCanceled();
        this.status = BookingStatus.CANCELED_BOOKING;
        //좌석을 취소한다.
        Events.raise(new BookingCanceledEvent(lineItems.stream().map(LineItem::getSeatId).toList(), memberId, bookingRef));
    }

    private void verifyNotCanceled() {
        if (!isNotCanceled())
            throw new AlreadyCanceledException();
    }

    private boolean isNotCanceled() {
        return status == BookingStatus.COMPLETED_BOOKING || status == BookingStatus.BEFORE_PAY;
    }

    public void confirmPayment(Long paymentId) {
        this.paymentId = paymentId;
        this.status = BookingStatus.COMPLETED_BOOKING;
        //TODO 예약 된 좌석의 상태를 이벤트로 변경한다.
        List<BookSeatId> seatIds = this.getLineItems().stream().map(LineItem::getSeatId).toList();
        Events.raise(new ConfirmPaymentEvent(seatIds));
    }
}
