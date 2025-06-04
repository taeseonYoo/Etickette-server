package com.tae.Etickette.session.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookingRef implements Serializable {
    private String value;

    public static BookingRef generate() {
        return new BookingRef(UUID.randomUUID().toString().substring(0, 5));
    }
    public BookingRef(String value) {
        this.value = value;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingRef)) return false;
        BookingRef that = (BookingRef) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
