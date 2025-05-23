package com.tae.Etickette.concert.application;

import com.tae.Etickette.concert.application.Dto.VenueCreateRequestDto;
import com.tae.Etickette.concert.application.Dto.VenueCreateResponseDto;
import com.tae.Etickette.concert.domain.Venue;
import com.tae.Etickette.concert.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;

    public Venue findById(Long venueId) {
        return venueRepository.findById(venueId).orElse(null);
    }

    @Transactional
    public VenueCreateResponseDto createVenue(VenueCreateRequestDto requestDto) {
        boolean duplicated = venueRepository.findVenueByAddress(requestDto.getAddress()).isPresent();
        if (duplicated) {
            throw new DuplicateKeyException("이미 등록된 공연장 입니다.");
        }
        Venue venue = requestDto.toEntity();

        Venue savedVenue = venueRepository.save(venue);

        return VenueCreateResponseDto.builder()
                .id(savedVenue.getId())
                .place(savedVenue.getPlace())
                .capacity(savedVenue.getCapacity())
                .address(savedVenue.getAddress())
                .build();
    }

}
