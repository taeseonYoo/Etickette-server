package com.tae.Etickette.bookseat.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSeatQueryService {
    private final BookSeatDataDao bookSeatDataDao;
    public List<BookSeatData> getAllSeat(Long sessionId) {
        return bookSeatDataDao.findBySessionId(sessionId);
    }
}
