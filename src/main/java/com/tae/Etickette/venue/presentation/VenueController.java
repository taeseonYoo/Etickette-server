package com.tae.Etickette.venue.presentation;

import com.tae.Etickette.venue.application.ChangeVenueService;
import com.tae.Etickette.venue.application.DeleteVenueService;
import com.tae.Etickette.venue.application.Dto.ChangeAddressRequest;
import com.tae.Etickette.venue.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.application.RegisterVenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/venues")
public class VenueController {
    private final ChangeVenueService changeVenueService;
    private final DeleteVenueService deleteVenueService;
    private final RegisterVenueService registerVenueService;

    /**
     * 공연장 등록
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> register(@RequestBody RegisterVenueRequest request) {
        registerVenueService.register(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 공연장 삭제
     * @param venueId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{venueId}")
    public ResponseEntity<Void> delete(@PathVariable Long venueId) {
        deleteVenueService.delete(venueId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 공연장 주소 수정
     * @param venueId
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{venueId}/address")
    public ResponseEntity<Void> changeAddress(@PathVariable Long venueId,
                                              @RequestBody ChangeAddressRequest request) {
        changeVenueService.changeAddress(venueId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
