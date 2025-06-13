package com.tae.Etickette.booking.domain;

import com.tae.Etickette.global.model.Canceller;

public interface CancelPolicy {
    boolean hasCancellationPermission(Booking booking, Canceller canceller);

    boolean hasEntireCancelPermission();
}
