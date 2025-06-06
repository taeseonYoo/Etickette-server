package com.tae.Etickette.venue.application;

import com.tae.Etickette.venue.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VenueQueryService {

    private final VenueRepository venueRepository;

    public Venue findById(Long venueId) {
        return venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException("공연장을 찾을 수 없습니다."));
    }
}
