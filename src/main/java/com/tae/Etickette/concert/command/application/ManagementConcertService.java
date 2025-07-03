package com.tae.Etickette.concert.command.application;

import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.infra.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagementConcertService {
    private final ConcertRepository concertRepository;

    @Transactional
    public void open(Long concertId) {
        Concert concert = concertRepository.findById(concertId).orElseThrow(() ->
                new ConcertNotFoundException("공연 정보를 찾을 수 없습니다."));
        //TODO ADMIN권한만 필요한 경우에는 PREAUTHORIZE 권장하자
        concert.open();
    }

}
