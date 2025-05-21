package com.tae.Etickette.concert.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VenueStatus {
    ACTIVE("ACTIVE"),
    DELETE("DELETE");
    private final String value;
}
