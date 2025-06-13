package com.tae.Etickette.bookseat.domain;

import lombok.Getter;

@Getter
public class Canceller {
    private Long memberId;
    public Canceller(Long memberId) {
        this.memberId = memberId;
    }
}
