package com.tae.Etickette.booking.infra;

import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.domain.BookingRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, BookingRef> {
    List<Booking> findBySessionId(Long sessionId);
}
