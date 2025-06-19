package com.tae.Etickette.concert.query.application;

import com.tae.Etickette.concert.query.dao.ConcertSummaryDao;
import com.tae.Etickette.concert.query.dto.ConcertSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConcertSummaryService {
    private final ConcertSummaryDao concertSummaryDao;

    public List<ConcertSummary> getAllConcerts() {
        return concertSummaryDao.findAll();
    }

    public Page<ConcertSummary> getPageList(Pageable pageable) {
        return concertSummaryDao.findAll(pageable);
    }
}
