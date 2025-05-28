package com.tae.Etickette.schedule.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScheduleStatus {
    BEFORE("BEFORE"),
    OPEN("OPEN"),
    COMPLETED("COMPLETED"),
    CANCELED("CANCELED");

    private final String value;
}
