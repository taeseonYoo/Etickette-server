package com.tae.Etickette.venue.presentation;

import com.tae.Etickette.venue.command.application.ChangeVenueService;
import com.tae.Etickette.venue.command.application.DeleteVenueService;
import com.tae.Etickette.venue.command.application.Dto.ChangeAddressRequest;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.command.application.RegisterVenueService;
import com.tae.Etickette.venue.query.VenueData;
import com.tae.Etickette.venue.query.VenueQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/venues")
public class VenueController {
    private final ChangeVenueService changeVenueService;
    private final DeleteVenueService deleteVenueService;
    private final RegisterVenueService registerVenueService;
    private final VenueQueryService venueQueryService;

    /**
     * 공연장 등록
     * @param request
     * @return
     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @DeleteMapping("/{venueId}")
    public ResponseEntity<Void> delete(@PathVariable Long venueId) {
        deleteVenueService.delete(venueId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 공연장 주소 수정
     * @param venueId
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{venueId}")
    public ResponseEntity<Void> changeAddress(@PathVariable Long venueId,
                                              @Valid @RequestBody ChangeAddressRequest request) {
        changeVenueService.changeAddress(venueId, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 사용 가능한 공연장 리스트를 반환한다.
     * @return
     */
    @GetMapping("")
    public ResponseEntity<List<VenueData>> getActivateVenueList() {
        return ResponseEntity.ok(venueQueryService.getActivateVenueList());
    }
}
