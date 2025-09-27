package com.tae.Etickette.session.application;

import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.SESSION_NOT_FOUND, "세션을 찾을 수 없습니다."));

        session.cancel();
    }

    @Transactional
    public void cancelByConcertId(Long concertId) {
        List<Session> sessions = sessionRepository.findAllByConcertId(concertId);

        sessions.forEach(Session::cancel);
    }
}
