package com.tae.Etickette.venue.query;

import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VenueQueryService {
    private final VenueRepository venueRepository;

    public List<ActivateVenueResponse> getActivateVenueList() {
        List<Venue> venueList = venueRepository.findAll();

        List<Venue> list = venueList.stream()
                .filter(Venue::isActivate).toList();

        if (list.isEmpty()) {
            throw new NoSuchElementException("공연장이 없습니다.");
        }

        List<ActivateVenueResponse> venueResponses = new ArrayList<>();
        for (Venue venue : list) {
            ActivateVenueResponse response = ActivateVenueResponse.builder()
                    .id(venue.getId())
                    .place(venue.getPlace())
                    .capacity(venue.getCapacity())
                    .address(venue.getAddress()).build();
            venueResponses.add(response);
        }
        return venueResponses;
    }
}
