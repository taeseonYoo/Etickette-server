package com.tae.Etickette.bookseat.infra;

import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookSeatRepository extends JpaRepository<BookSeat, BookSeatId> {
}
