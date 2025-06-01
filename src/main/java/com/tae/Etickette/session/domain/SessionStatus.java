package com.tae.Etickette.session.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SessionStatus {
    BEFORE("BEFORE"),
    OPEN("OPEN"),
    COMPLETED("COMPLETED"),
    CANCELED("CANCELED");

    private final String value;
}
