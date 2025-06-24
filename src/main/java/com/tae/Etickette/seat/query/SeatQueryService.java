package com.tae.Etickette.seat.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatQueryService {
    private final SeatDataDao seatDataDao;

    public Optional<SeatData> getSeat(Long seatId) {
        return seatDataDao.findById(seatId);
    }
}
