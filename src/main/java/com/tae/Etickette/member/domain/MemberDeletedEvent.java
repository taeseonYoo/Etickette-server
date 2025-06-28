package com.tae.Etickette.member.domain;

import com.tae.Etickette.global.event.Event;
import lombok.Getter;

@Getter
public class MemberDeletedEvent extends Event {
    private String email;
    public MemberDeletedEvent(String email) {
        super();
        this.email = email;
    }
}
