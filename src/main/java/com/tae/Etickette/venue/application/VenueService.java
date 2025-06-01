package com.tae.Etickette.venue.application;

import com.tae.Etickette.venue.application.Dto.VenueCreateRequestDto;
import com.tae.Etickette.venue.application.Dto.VenueCreateResponseDto;
import com.tae.Etickette.concert.domain.Seat;
import com.tae.Etickette.venue.domain.Venue;
import com.tae.Etickette.concert.infra.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;

    public Venue findById(Long venueId) {
        return venueRepository.findById(venueId).orElseThrow(()-> new VenueNotFoundException("공연장을 찾을 수 없습니다."));
    }

    public void findAll() {
        //TODO DTO가 나갈 수 있도록?
        return;
    }

    @Transactional
    public VenueCreateResponseDto createVenue(VenueCreateRequestDto requestDto) {
        boolean duplicated = venueRepository.findVenueByAddress(requestDto.getAddress()).isPresent();
        if (duplicated) {
            throw new DuplicateKeyException("이미 등록된 공연장 입니다.");
        }
        Venue venue = requestDto.toEntity();

        Venue savedVenue = venueRepository.save(venue);

        List<Seat> seats = initSeat(venue);
        savedVenue.addSeats(seats);

        return VenueCreateResponseDto.builder()
                .id(savedVenue.getId())
                .place(savedVenue.getPlace())
                .capacity(savedVenue.getCapacity())
                .address(savedVenue.getAddress())
                .build();
    }

    /**
     * 공연장 삭제
     * @param venueId 공연장Id
     */
    @Transactional
    public void deleteVenue(Long venueId) {
        Venue findVenue = findById(venueId);

        findVenue.deleteVenue();
    }


    private List<Seat> initSeat(Venue venue) {

        List<Seat> init = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j <= 10; j++) {
                init.add(Seat.create(String.valueOf((char)('A' + i)), j, "VIP", venue));
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j <= 10; j++) {
                init.add(Seat.create(String.valueOf((char)('F' + i)), j, "S", venue));
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j <= 10; j++) {
                init.add(Seat.create(String.valueOf((char)('K' + i)), j, "R", venue));
            }
        }
        return init;
    }

}
