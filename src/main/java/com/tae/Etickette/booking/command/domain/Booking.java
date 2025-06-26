package com.tae.Etickette.booking.command.domain;

import com.tae.Etickette.bookseat.command.domain.BookSeatId;
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
    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;
    @Convert(converter = MoneyConverter.class)
    private Money totalAmounts;

    @Column
    private Long memberId;
    @Column
    private Long sessionId;
    @Column(nullable = true)
    private Long paymentId;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "line_item",joinColumns = @JoinColumn(name = "booking_id"))
    private List<SeatItem> seatItems;

    public Booking(Long memberId, Long sessionId, List<SeatItem> seatItems) {
        this.bookingRef = BookingRef.generate();
        this.memberId = memberId;
        this.sessionId = sessionId;
        setSeatItems(seatItems);

        status = BookingStatus.BEFORE_PAY;
    }

    public static Booking create(Long memberId, Long sessionId, List<SeatItem> seatItems) {
        return new Booking(memberId, sessionId, seatItems);
    }

    private void setSeatItems(List<SeatItem> seatItems) {
        verifySelectedSeatCount(seatItems.size());
        this.seatItems = seatItems;
        calculateTotalAmounts();
    }
    //예매 가능한 좌석의 최대 개수는 4개
    private void verifySelectedSeatCount(int count){
        if (count > 4 || count ==0) {
            throw new InvalidMaxSeatCountException("좌석은 최대 4개까지 선택 가능합니다.");
        }
    }
    //좌석의 총금액을 계산한다.
    private void calculateTotalAmounts() {
        this.totalAmounts = new Money(seatItems.stream().mapToInt(x -> x.getPrice().getValue()).sum());
    }

    /**
     * 예매를 취소한다.
     * 예매를 취소하면, 좌석을 취소하는 이벤트가 발행된다.
     */
    public void cancel() {
        verifyNotCanceled();
        this.status = BookingStatus.CANCELED_BOOKING;
        //좌석을 취소한다.
        Events.raise(new BookingCanceledEvent(seatItems.stream().map(SeatItem::getSeatId).toList(), memberId, bookingRef));
    }
    private void verifyNotCanceled() {
        if (!isNotCanceled())
            throw new AlreadyCanceledException();
    }
    private boolean isNotCanceled() {
        return status == BookingStatus.COMPLETED_BOOKING || status == BookingStatus.BEFORE_PAY;
    }

    /**
     * 예매가 결제완료된다.
     * @param paymentId
     * 결제가 완료되면, 예약 된 좌석의 상태를 변경한다.
     */
    public void confirmPayment(Long paymentId) {
        verifyNotYetPaid();
        this.paymentId = paymentId;
        this.status = BookingStatus.COMPLETED_BOOKING;

        List<BookSeatId> seatIds = this.getSeatItems().stream().map(SeatItem::getSeatId).toList();
        Events.raise(new ConfirmPaymentEvent(seatIds));
    }
    private void verifyNotYetPaid() {
        if (!isNotYetPaid())
            throw new BookingCompletionFailedException("예매를 완료할 수 없습니다.");
    }
    private boolean isNotYetPaid() {
        return status == BookingStatus.BEFORE_PAY;
    }
}
