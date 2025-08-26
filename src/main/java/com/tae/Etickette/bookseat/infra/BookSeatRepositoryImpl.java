package com.tae.Etickette.bookseat.infra;

import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.command.domain.CustomBookSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookSeatRepositoryImpl implements CustomBookSeatRepository {
    private final JdbcTemplate jdbcTemplate;
    @Transactional
    @Override
    public void saveAllInBulk(List<BookSeat> bookSeats) {
        String sql = "INSERT INTO book_seat (seat_id,session_id,grade,price,status) " +
                "VALUES (?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql,
                bookSeats,
                bookSeats.size(),
                (PreparedStatement ps, BookSeat seat) -> {
                    ps.setLong(1,seat.getSeatId());
                    ps.setLong(2,seat.getSessionId());
                    ps.setString(3,seat.getGrade());
                    ps.setInt(4,seat.getPrice().getValue());
                    ps.setString(5, seat.getStatus().getValue());
                });
    }
}
