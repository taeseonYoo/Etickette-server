package com.tae.Etickette.booking.command.domain;

import com.tae.Etickette.global.model.Canceller;

public interface CancelPolicy {
    boolean hasCancellationPermission(Booking booking, Canceller canceller);

    boolean hasEntireCancelPermission();
}
