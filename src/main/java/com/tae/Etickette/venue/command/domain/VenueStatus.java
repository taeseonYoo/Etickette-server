package com.tae.Etickette.venue.command.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VenueStatus {
    ACTIVE("ACTIVE"), //사용 가능
    DELETED("DELETED"); //삭제 된 공연장
    private final String value;
}
