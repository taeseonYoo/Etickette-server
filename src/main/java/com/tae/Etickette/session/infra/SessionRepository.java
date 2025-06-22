package com.tae.Etickette.session.infra;

import com.tae.Etickette.session.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session,Long> {
    List<Session> findAllByConcertId(Long concertId);

    @Query("""
SELECT s FROM Session s 
JOIN Concert c ON s.concertId=c.id
WHERE c.venueId = :venueId
""")
    List<Session> findAllByVenueId(Long venueId);
}
