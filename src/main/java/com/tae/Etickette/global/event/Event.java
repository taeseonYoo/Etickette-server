package com.tae.Etickette.global.event;

import lombok.Getter;

@Getter
public abstract class Event {
    private long timestamp;

    public Event() {
        this.timestamp = System.currentTimeMillis();
    }
}
