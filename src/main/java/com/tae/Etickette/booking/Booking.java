package com.tae.Etickette.booking;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "BookedSeat",
            joinColumns = @JoinColumn(name = "Booking_number"))
    private List<BookedSeat> bookedSeats;

    @Convert(converter = MoneyConverter.class)
    @Column(name = "total_amounts")
    private Money totalAmounts;

    private Long scheduleId;
    private Long memberId;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public Booking(List<BookedSeat> bookedSeats, Long scheduleId,Long memberId) {
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.status = BookingStatus.PENDING;
        setBookedSeats(bookedSeats);
    }

    public void setBookedSeats(List<BookedSeat> bookedSeats) {
        verifySeatsSize(bookedSeats);
        this.bookedSeats = bookedSeats;
        calculateTotalAmounts();
    }

    private void verifySeatsSize(List<BookedSeat> bookedSeats) {
        if (bookedSeats.size() > 2) {
            throw new TooManyItemsException("좌석은 한 번에 2개만 예매할 수 있습니다.");
        } else if (bookedSeats.isEmpty()) {
            throw new IllegalArgumentException("예약할 좌석이 없습니다.");
        }
    }

    private void calculateTotalAmounts() {
        this.totalAmounts = new Money(bookedSeats.stream().mapToInt(x -> x.getPrice().getValue()).sum());
    }
    public void cancel() {
        cancelBookedSeat();
    }

    private void cancelBookedSeat() {
        bookedSeats.clear();
    }
}
