package com.tae.Etickette.session.application;

import com.tae.Etickette.session.application.Dto.SessionRegisterReqDto;
import com.tae.Etickette.session.domain.Seat;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionRegisterService {
    private final SessionRepository sessionRepository;

    @Transactional
    public void registerSession(SessionRegisterReqDto requestDto) {
        //공연장 중복 확인

        Session session = Session.create(requestDto.getConcertDate(),
                requestDto.getStartTime(),
                requestDto.getRunningTime(),
                requestDto.getConcertId(),
                requestDto.getVenueId(),
                initSeatingPlan()
        );

        sessionRepository.save(session);
    }


    public List<Seat> initSeatingPlan() {
        List<Seat> seats = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            seats.add(new Seat(String.valueOf((char) ('A' + (i - 1) / 10)), ((i - 1) % 10) + 1, "VIP"));
        }

        return seats;
    }

}
