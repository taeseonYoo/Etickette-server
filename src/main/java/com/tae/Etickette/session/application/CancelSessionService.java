package com.tae.Etickette.session.application;

import com.tae.Etickette.session.domain.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CancelSessionService {
    private final SessionFinder sessionFinder;
    @Transactional
    public void cancel(Long sessionId) {
        Session session = sessionFinder.findSessionByIdOrThrow(sessionId);

        session.cancel();
    }

    @Transactional
    public void cancelByConcertId(Long concertId) {
        List<Session> sessions = sessionFinder.findAllByConcertId(concertId);

        sessions.forEach(Session::cancel);
    }
}
