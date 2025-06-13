package com.tae.Etickette.bookseat.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeatStatus {
    AVAILABLE("AVAILABLE"),
    LOCKED("LOCKED"),
    BOOKED("BOOKED");
    private final String value;
}
