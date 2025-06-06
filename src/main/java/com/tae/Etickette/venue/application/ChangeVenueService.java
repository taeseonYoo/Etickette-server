package com.tae.Etickette.venue.application;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.application.Dto.ChangeAddressRequestDto;
import com.tae.Etickette.venue.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tae.Etickette.venue.application.VenueServiceHelper.*;

@Service
@RequiredArgsConstructor
public class ChangeVenueService {
    private final VenueRepository venueRepository;

    @Transactional
    public void changeAddress(ChangeAddressRequestDto requestDto) {
        Venue venue = venueRepository.findById(requestDto.getVenueId())
                .orElseThrow(() -> new VenueNotFoundException("공연장을 찾을 수 없습니다."));

        Address newAddress = new Address(
                requestDto.getCity(),
                requestDto.getStreet(),
                requestDto.getZipcode());

        verifyDuplicateAddress(venueRepository, newAddress);

        venue.changeAddress(newAddress);
    }
}
