package com.tae.Etickette.booking.command.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookingRef implements Serializable {
    @Column(name = "booking_ref")
    private String value;

    public static BookingRef generate() {
        LocalDate today = LocalDate.now();
        String format = today.format(DateTimeFormatter.ofPattern("MMdd"));
        return new BookingRef(format + UUID.randomUUID().toString().substring(0, 5));
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
