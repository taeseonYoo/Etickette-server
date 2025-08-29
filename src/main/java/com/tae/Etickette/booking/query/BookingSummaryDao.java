package com.tae.Etickette.booking.query;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface BookingSummaryDao extends Repository<BookingSummary,String> {
    List<BookingSummary> findBookingSummariesByMemberId(Long memberId);
}
