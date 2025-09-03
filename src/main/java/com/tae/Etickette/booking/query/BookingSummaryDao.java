package com.tae.Etickette.booking.query;

import com.tae.Etickette.booking.command.domain.BookingStatus;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface BookingSummaryDao extends Repository<BookingSummary,String> {
    List<BookingSummary> findBookingSummariesByMemberId(Long memberId);
    List<BookingSummary> findBookingSummariesByMemberIdAndStatus(Long memberId, BookingStatus bookingStatus);
}
