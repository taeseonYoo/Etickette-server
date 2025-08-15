package com.tae.Etickette.concert.command.application;

import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagementConcertService {
    private final ConcertRepository concertRepository;

    //공연을 사용자들이 볼 수 있게 오픈한다.
    @Transactional
    public void open(Long concertId) {
        Concert concert = concertRepository.findById(concertId).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.CONCERT_NOT_FOUND, "공연 정보를 찾을 수 없습니다."));

        concert.open();
    }

}
