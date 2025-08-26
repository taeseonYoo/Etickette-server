package com.tae.Etickette.bookseat.infra;

import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.bookseat.command.domain.CustomBookSeatRepository;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookSeatRepository extends JpaRepository<BookSeat, BookSeatId>, CustomBookSeatRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from BookSeat b where b.seatId = :seatId and b.sessionId = :sessionId")
    Optional<BookSeat> findByIdWithLock(Long seatId, Long sessionId);

}
