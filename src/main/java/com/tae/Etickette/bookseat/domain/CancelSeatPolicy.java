package com.tae.Etickette.bookseat.domain;

import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.global.model.Canceller;

public interface CancelSeatPolicy {
    boolean hasCancellationPermission(Booking booking, Canceller canceller);
}
