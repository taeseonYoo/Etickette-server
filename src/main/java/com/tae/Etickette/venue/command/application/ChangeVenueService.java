package com.tae.Etickette.venue.command.application;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.command.application.Dto.ChangeAddressRequest;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeVenueService {
    private final VenueRepository venueRepository;

    @Transactional
    public void changeAddress(Long venueId,ChangeAddressRequest requestDto) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException("공연장을 찾을 수 없습니다."));

        Address newAddress = requestDto.getAddress();

        //중복된 주소가 존재하는 지 검사
        VenueServiceHelper.verifyDuplicateAddress(venueRepository, newAddress);

        venue.changeAddress(newAddress);
    }
}
