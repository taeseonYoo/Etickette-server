package com.tae.Etickette.session.infra;

import com.tae.Etickette.session.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session,Long> {
    List<Session> findByVenueId(Long venueId);
    List<Session> findAllByConcertId(Long concertId);
}
