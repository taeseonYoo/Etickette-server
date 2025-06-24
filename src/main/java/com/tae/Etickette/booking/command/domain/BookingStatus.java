package com.tae.Etickette.booking.command.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {
    //결제전,예매완료,예매취소
    BEFORE_PAY("BEFORE_PAY"),
    COMPLETED_BOOKING("COMPLETED_BOOKING"),
    CANCELED_BOOKING("CANCELED_BOOKING");
    private final String value;
}
