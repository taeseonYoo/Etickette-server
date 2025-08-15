package com.tae.Etickette.venue.command.application;

import com.tae.Etickette.global.exception.BadRequestException;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ForbiddenException;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.venue.command.domain.DeleteVenuePolicy;
import com.tae.Etickette.venue.command.domain.Venue;
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
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.VENUE_NOT_FOUND,"공연장을 찾을 수 없습니다."));

        if (!deleteVenuePolicy.hasDeletePermission()) {
            throw new ForbiddenException(ErrorCode.NO_PERMISSION,"삭제 권한이 없습니다.");
        }

        venue.deleteVenue();
    }
}
