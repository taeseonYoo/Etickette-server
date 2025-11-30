package com.tae.Etickette.concert.command.application;

import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertFinder {
    private final ConcertRepository concertRepository;

    public Concert findByIdOrThrow(Long concertId) {
        return concertRepository.findById(concertId).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.CONCERT_NOT_FOUND, "공연을 찾을 수 없습니다."));
    }
}
