package com.tae.Etickette.concert;

import com.tae.Etickette.common.model.Money;
import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.domain.GradePrice;
import com.tae.Etickette.concert.infra.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConcertRegisterService {
    private final ConcertRepository concertRepository;

    @Transactional
    public void registerConcert(ConcertRegisterReqDto requestDto) {

        List<GradePrice> gradePrices = requestDto.getGradePrices().stream()
                .map(info -> new GradePrice(info.getGrade(), new Money(info.getPrice())))
                .toList();

        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                requestDto.getRunningTime(),
                requestDto.getImgURL(),
                gradePrices
        );

        concertRepository.save(concert);
    }
}
