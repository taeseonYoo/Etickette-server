package com.tae.Etickette.booking.query;

import com.tae.Etickette.booking.domain.Booking;
import lombok.Getter;

import java.util.List;

@Getter
public class PaymentInfo {
    private final String bookingRef;
    private final int totalAmounts;
    private final List<LineItemInfo> details;

    public PaymentInfo(Booking booking, List<LineItemInfo>details) {
        this.bookingRef = booking.getBookingRef().getValue();
        this.totalAmounts = booking.getTotalAmounts().getValue();
        this.details = details;
    }
}
