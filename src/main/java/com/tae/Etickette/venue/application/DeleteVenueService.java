package com.tae.Etickette.venue.application;

import com.tae.Etickette.venue.domain.DeleteVenuePolicy;
import com.tae.Etickette.venue.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteVenueService {
    private final VenueRepository venueRepository;
    private final DeleteVenuePolicy deleteVenuePolicy;

    @Transactional
    public void delete(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException("공연장을 찾을 수 없습니다."));

        if (!deleteVenuePolicy.hasDeletePermission()) {
            throw new NoDeletablePermission("삭제 권한이 없습니다.");
        }

        venue.deleteVenue();
    }
}
