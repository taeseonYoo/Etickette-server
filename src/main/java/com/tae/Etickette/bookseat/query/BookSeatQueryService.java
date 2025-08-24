package com.tae.Etickette.bookseat.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSeatQueryService {
    private final BookSeatDataDao bookSeatDataDao;

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<BookSeatData> getAllSeat(Long sessionId) {
        return bookSeatDataDao.findBySessionId(sessionId);
    }
}
