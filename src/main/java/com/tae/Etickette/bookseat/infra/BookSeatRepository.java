package com.tae.Etickette.bookseat.infra;

import com.tae.Etickette.bookseat.domain.BookSeat;
import com.tae.Etickette.bookseat.domain.BookSeatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookSeatRepository extends JpaRepository<BookSeat, BookSeatId> {
}
