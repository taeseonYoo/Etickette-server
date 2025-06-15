package com.tae.Etickette.venue.application;

import com.tae.Etickette.venue.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.application.Dto.RegisterVenueResponse;
import com.tae.Etickette.venue.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tae.Etickette.venue.application.VenueServiceHelper.*;


@Service
@RequiredArgsConstructor
public class RegisterVenueService {
    private final VenueRepository venueRepository;

    @Transactional
    public RegisterVenueResponse register(RegisterVenueRequest requestDto) {
        //중복 검사
        verifyDuplicateAddress(venueRepository, requestDto.getAddress());

        Venue savedVenue = venueRepository.save(requestDto.toEntity());

        return RegisterVenueResponse.builder()
                .id(savedVenue.getId())
                .place(savedVenue.getPlace())
                .capacity(savedVenue.getCapacity())
                .address(savedVenue.getAddress())
                .build();
    }

}
