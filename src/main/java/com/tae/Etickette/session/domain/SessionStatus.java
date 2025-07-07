package com.tae.Etickette.session.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SessionStatus {
    AVAILABLE("AVAILABLE"),
    CANCELED("CANCELED");

    private final String value;
}
