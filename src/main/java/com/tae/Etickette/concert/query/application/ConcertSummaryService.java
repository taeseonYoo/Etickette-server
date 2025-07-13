package com.tae.Etickette.concert.query.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tae.Etickette.concert.query.dao.ConcertSummaryDao;
import com.tae.Etickette.concert.query.dto.ConcertSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ConcertSummaryService {
    private final ConcertSummaryDao concertSummaryDao;
    public List<ConcertSummary> getAllConcerts() {
        return concertSummaryDao.findAll();
    }

    @Cacheable(value = "concerts", key = "'concerts:'+#pageable.pageNumber + '-' + #pageable.pageSize+':summaries'")
    public List<ConcertSummary> getPageList(Pageable pageable) {
        return concertSummaryDao.findAll(pageable);
    }
}
