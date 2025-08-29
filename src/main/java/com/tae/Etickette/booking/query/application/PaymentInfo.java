package com.tae.Etickette.booking.query.application;

import com.tae.Etickette.booking.command.domain.Booking;
import lombok.Getter;

import java.util.List;

@Getter
public class PaymentInfo {
    private final String bookingRef;
    private final int totalAmounts;
    private final List<LineItemInfo> details;
    private final String bookerName;
    private final String bookerEmail;

    public PaymentInfo(Booking booking, List<LineItemInfo>details, Booker booker) {
        this.bookingRef = booking.getBookingRef().getValue();
        this.totalAmounts = booking.getTotalAmounts().getValue();
        this.details = details;
        this.bookerName = booker.getName();
        this.bookerEmail = booker.getEmail();
    }
}
