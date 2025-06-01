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


    @Convert(converter = MoneyConverter.class)
    @Column(name = "total_amounts")
    private Money totalAmounts;

    private Long scheduleId;
    private Long memberId;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public Booking(Long scheduleId,Long memberId) {
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.status = BookingStatus.PENDING;

    }



}
