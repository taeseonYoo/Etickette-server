package com.tae.Etickette.concert.command.application;

import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertResponse;
import com.tae.Etickette.concert.command.domain.*;
import com.tae.Etickette.global.event.Events;
import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.concert.infra.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegisterConcertService {
    private final ConcertRepository concertRepository;
    private final ImageUploader imageUploader;
    @Transactional
    public RegisterConcertResponse register(RegisterConcertRequest requestDto, MultipartFile imageFile){
        //가격 정보
        List<GradePrice> gradePrices = requestDto.getGradePrices().stream()
                .map(info -> new GradePrice(info.getGrade(), new Money(info.getPrice())))
                .toList();

        //이미지 업로드
        String uploadedURL = imageUploader.upload(imageFile);

        Concert concert = Concert.create(requestDto.getTitle(),
                requestDto.getOverview(),
                requestDto.getRunningTime(),
                new Image(uploadedURL),
                gradePrices,
                requestDto.getVenueId()
        );
        Concert savedConcert = concertRepository.save(concert);

        //좌석을 등록하는 이벤트 발행
        Events.raise(new ConcertCreatedEvent(savedConcert.getId()));

        return RegisterConcertResponse.builder().concertId(concert.getId()).build();
    }


}
