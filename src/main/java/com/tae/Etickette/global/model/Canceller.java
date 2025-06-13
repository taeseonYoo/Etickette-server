package com.tae.Etickette.global.model;

import lombok.Getter;

@Getter
public class Canceller {
    private Long memberId;
    public Canceller(Long memberId) {
        this.memberId = memberId;
    }
}
