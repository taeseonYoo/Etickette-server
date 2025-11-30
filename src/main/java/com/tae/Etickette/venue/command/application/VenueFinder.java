package com.tae.Etickette.venue.command.application;

import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueFinder {
    private final VenueRepository venueRepository;

    public Venue findVenueByIdOrThrow(Long venueId) {
        return venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.VENUE_NOT_FOUND,
                        "공연장을 찾을 수 없습니다. 공연장 Id =" + venueId));
    }
}
