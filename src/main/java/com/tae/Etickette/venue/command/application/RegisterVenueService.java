package com.tae.Etickette.venue.command.application;

import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueResponse;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RegisterVenueService {
    private final VenueRepository venueRepository;

    @Transactional
    public RegisterVenueResponse register(RegisterVenueRequest requestDto) {
        //중복 검사
        VenueServiceHelper.verifyDuplicateAddress(venueRepository, requestDto.getAddress());

        Venue savedVenue = venueRepository.save(Venue.create(requestDto.getPlace(), requestDto.getCapacity(), requestDto.getAddress()));

        return RegisterVenueResponse.builder()
                .id(savedVenue.getId())
                .place(savedVenue.getPlace())
                .capacity(savedVenue.getCapacity())
                .address(savedVenue.getAddress())
                .build();
    }

}
