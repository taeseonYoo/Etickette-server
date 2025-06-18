package com.tae.Etickette.concert.query.application;

import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.infra.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConcertDetailService {
    private final ConcertRepository concertRepository;

//    public void getConcertDetail(Long concertId) {
//        Optional<Concert> concertOpt = concertRepository.findById(concertId);
//        return concertOpt.map(concert->{
//
//        })
//    }
}
