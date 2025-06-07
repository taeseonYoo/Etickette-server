package com.tae.Etickette.session.domain;

import com.tae.Etickette.global.event.Event;
import lombok.Getter;

@Getter
public class SessionCanceledEvent extends Event {
    private Long sessionId;
    public SessionCanceledEvent(Long sessionId) {
        super();
        this.sessionId = sessionId;
    }
}
