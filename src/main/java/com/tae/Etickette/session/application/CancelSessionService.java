package com.tae.Etickette.session.application;

import com.tae.Etickette.booking.command.application.SessionNotFoundException;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CancelSessionService {
    private final SessionRepository sessionRepository;
    @Transactional
    public void cancel(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("세션을 찾을 수 없습니다."));

        session.cancel();
    }

    @Transactional
    public void cancelByConcertId(Long concertId) {
        List<Session> sessions = sessionRepository.findAllByConcertId(concertId);

        for (Session session : sessions) {
            session.cancel();
        }
    }
}
