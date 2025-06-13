package com.tae.Etickette.bookseat.domain;

import com.tae.Etickette.booking.domain.Booking;
import com.tae.Etickette.global.model.Canceller;

public interface CancelSeatPolicy {
    boolean hasCancellationPermission(Booking booking, Canceller canceller);
}
