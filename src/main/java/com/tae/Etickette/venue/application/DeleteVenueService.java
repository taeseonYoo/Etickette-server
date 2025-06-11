package com.tae.Etickette.venue.application;

import com.tae.Etickette.venue.application.Dto.DeleteVenueRequest;
import com.tae.Etickette.venue.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteVenueService {
    private final VenueRepository venueRepository;

    @Transactional
    public void delete(DeleteVenueRequest requestDto) {
        //TODO 권한 검증은 컨트롤러?
        Venue venue = venueRepository.findById(requestDto.getVenueId())
                .orElseThrow(() -> new VenueNotFoundException("공연장을 찾을 수 없습니다."));

        venue.deleteVenue();
    }
}
