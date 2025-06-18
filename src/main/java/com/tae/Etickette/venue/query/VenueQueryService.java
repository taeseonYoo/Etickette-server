package com.tae.Etickette.venue.query;

import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VenueQueryService {
    private final VenueDataDao venueDataDao;

    public List<VenueData> getActivateVenueList() {
        Specification<VenueData> specs = VenueDataSpecs.statusActive(Venue.getActiveInfo());
        return venueDataDao.findAll(specs);
    }

    public Optional<VenueData> getVenue(Long venueId) {
        return venueDataDao.findById(venueId);
    }
}
