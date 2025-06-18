package com.tae.Etickette.venue.query;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface VenueDataDao extends Repository<VenueData, Long> {
    Optional<VenueData> findById(Long venueId);

    List<VenueData> findAll(Specification<VenueData> spec);
}
