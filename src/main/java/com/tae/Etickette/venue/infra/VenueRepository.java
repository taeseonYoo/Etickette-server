package com.tae.Etickette.venue.infra;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.domain.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Venue> findVenueByAddress(Address address);
}
