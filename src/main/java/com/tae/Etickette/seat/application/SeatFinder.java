package com.tae.Etickette.seat.application;

import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.seat.infra.SeatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatFinder {
    private final SeatRepository seatRepository;

    public List<Long> findIdsByConcertId(Long concertId) {
        List<Long> seatIds = seatRepository.findIdByConcertId(concertId);
        if (seatIds.isEmpty()) {
            throw new ResourceNotFoundException(ErrorCode.SEAT_NOT_FOUND, "좌석 정보를 찾을 수 없습니다.");
        }
        return seatIds;
    }
}
