package com.tae.Etickette.seat.infra;

import com.tae.Etickette.seat.domain.CustomSeatRepository;
import com.tae.Etickette.seat.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements CustomSeatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void saveAllInBulk(List<Seat> seats) {
        String sql = "INSERT INTO seat (seat_column,seat_row,concert_id,locx,locy)" +
                "VALUES (?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql,
                seats,
                seats.size(),
                (PreparedStatement ps, Seat seat) -> {
                    ps.setString(1, seat.getColumn());
                    ps.setString(2, seat.getRow());
                    ps.setLong(3, seat.getConcertId());
                    ps.setString(4, seat.getLocX());
                    ps.setString(5, seat.getLocY());
                });
    }
}
