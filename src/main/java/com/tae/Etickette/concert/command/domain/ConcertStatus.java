package com.tae.Etickette.concert.command.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConcertStatus {
    BEFORE("BEFORE"),
    OPEN("OPEN"),
    COMPLETED("COMPLETED"),
    CANCELED("CANCELED");
    private final String value;
}
