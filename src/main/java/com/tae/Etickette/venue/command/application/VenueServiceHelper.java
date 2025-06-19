package com.tae.Etickette.venue.command.application;

import com.tae.Etickette.concert.command.domain.Address;
import com.tae.Etickette.venue.infra.VenueRepository;
import org.springframework.dao.DuplicateKeyException;

/**
 * 중복된 응용 서비스 로직을 구현한 헬퍼 클래스
 */
public final class VenueServiceHelper {
    //중복된 주소가 존재하는 지 검증한다.
    public static void verifyDuplicateAddress(VenueRepository repo, Address newAddress) {
        boolean duplicated = repo.findVenueByAddress(newAddress).isPresent();
        if (duplicated) {
            throw new DuplicateKeyException("이미 등록된 공연장 입니다.");
        }
    }
}
