package com.tae.Etickette.session.application;

import com.tae.Etickette.global.exception.ConflictException;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.infra.SessionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 세션의 공통된 로직을 구분한 헬퍼 클래스
 */
public final class SessionServiceHelper {
    public static void findExistingDate(SessionRepository repo, Long venueId, List<LocalDate> requestDates) {
        Set<LocalDate> existingDates = repo.findAllByVenueId(venueId).stream()
                .map(Session::getConcertDate)
                .collect(Collectors.toSet());

        for (LocalDate date : requestDates) {
            if (existingDates.contains(date)) {
                throw new ConflictException(ErrorCode.DUPLICATE_DATE, "이미 스케줄이 존재합니다." + date);
            }
        }
    }
}
