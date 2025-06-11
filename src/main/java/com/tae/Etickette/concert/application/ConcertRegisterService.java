package com.tae.Etickette.concert.application;

import com.tae.Etickette.concert.application.dto.RegisterConcertRequest;
import com.tae.Etickette.global.model.Money;
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
    public Long register(RegisterConcertRequest requestDto) {

        List<GradePrice> gradePrices = requestDto.getGradePrices().stream()
                .map(info -> new GradePrice(info.getGrade(), new Money(info.getPrice())))
                .toList();

        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                requestDto.getRunningTime(),
                requestDto.getImgURL(),
                gradePrices
        );

        return concertRepository.save(concert).getId();
    }
}
