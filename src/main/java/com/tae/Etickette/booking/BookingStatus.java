package com.tae.Etickette.booking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookingStatus {
    PENDING("PENDING"), //결제 대기 상태
    COMPLETED("COMPLETED"),
    CANCELED("CANCELED");
    private final String value;
}
