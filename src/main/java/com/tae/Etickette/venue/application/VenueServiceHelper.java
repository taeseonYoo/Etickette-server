package com.tae.Etickette.venue.application;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.infra.VenueRepository;
import org.springframework.dao.DuplicateKeyException;

public final class VenueServiceHelper {
    public static void verifyDuplicateAddress(VenueRepository repo, Address newAddress) {
        boolean duplicated = repo.findVenueByAddress(newAddress).isPresent();
        if (duplicated) {
            throw new DuplicateKeyException("이미 등록된 공연장 입니다.");
        }
    }
}
