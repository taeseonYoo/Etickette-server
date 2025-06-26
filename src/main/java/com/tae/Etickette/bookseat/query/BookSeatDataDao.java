package com.tae.Etickette.bookseat.query;

import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface BookSeatDataDao extends Repository<BookSeatData, BookSeatId> {
    List<BookSeatData> findBySessionId(Long sessionId);

}
