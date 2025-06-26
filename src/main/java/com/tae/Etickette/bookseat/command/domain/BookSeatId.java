package com.tae.Etickette.bookseat.command.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookSeatId implements Serializable {
    private Long seatId;
    private Long sessionId;

    public BookSeatId(Long seatId, Long sessionId) {
        this.seatId = seatId;
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookSeatId that = (BookSeatId) o;
        return Objects.equals(seatId, that.seatId) && Objects.equals(sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatId, sessionId);
    }
}
