package com.tae.Etickette.venue.query;

import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
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
        Specification<VenueData> specs = VenueDataSpecs.statusActive();
        return venueDataDao.findAll(specs);
    }

    public VenueData findVenueByIdOrThrow(Long venueId) {
        return venueDataDao.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.VENUE_NOT_FOUND, "공연장 정보를 찾을 수 없습니다."));
    }

}
