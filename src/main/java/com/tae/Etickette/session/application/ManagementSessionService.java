package com.tae.Etickette.session.application;

import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementSessionService {
    private final SessionRepository sessionRepository;

    /**
     * 공연에 등록된 스케줄을 오픈한다.
     * @param concertId
     */

}
