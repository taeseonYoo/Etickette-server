package com.tae.Etickette.concert.query.dao;

import com.tae.Etickette.concert.query.dto.ConcertSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ConcertSummaryDao extends Repository<ConcertSummary, Long> {
    List<ConcertSummary> findAll();

    List<ConcertSummary> findAll(Pageable pageable);

}
