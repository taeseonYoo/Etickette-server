package com.tae.Etickette.session.application;

import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionFinder {
    private final SessionRepository sessionRepository;

    public Session findSessionByIdOrThrow(Long sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.SESSION_NOT_FOUND,
                        "세션을 찾을 수 없습니다. 세션 번호:" + sessionId));
    }

    public List<Session> findAllByConcertId(Long concertId) {
        return sessionRepository.findAllByConcertId(concertId);
    }
}
