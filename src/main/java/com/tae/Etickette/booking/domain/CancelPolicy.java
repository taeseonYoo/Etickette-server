package com.tae.Etickette.booking.domain;

import com.tae.Etickette.member.domain.Member;

public interface CancelPolicy {
    boolean hasCancellationPermission(Booking booking, Member member);
}
