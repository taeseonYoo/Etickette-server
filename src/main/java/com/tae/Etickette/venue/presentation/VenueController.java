package com.tae.Etickette.venue.presentation;

import com.tae.Etickette.venue.command.application.ChangeVenueService;
import com.tae.Etickette.venue.command.application.DeleteVenueService;
import com.tae.Etickette.venue.command.application.Dto.ChangeAddressRequest;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.command.application.RegisterVenueService;
import com.tae.Etickette.venue.query.VenueData;
import com.tae.Etickette.venue.query.VenueQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Venue API", description = "공연장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/venues")
public class VenueController {
    private final ChangeVenueService changeVenueService;
    private final DeleteVenueService deleteVenueService;
    private final RegisterVenueService registerVenueService;
    private final VenueQueryService venueQueryService;

    @Operation(summary = "공연장 등록", description = "[ADMIN] 장소명, 수용 인원, 주소를 입력하여 공연장을 등록한다.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterVenueRequest request) {
        registerVenueService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "공연장 삭제", description = "공연장Id를 입력받아 공연장을 삭제한다.")
    @DeleteMapping("/{venueId}")
    public ResponseEntity<Void> delete(@PathVariable Long venueId) {
        deleteVenueService.delete(venueId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공연장 주소 변경", description = "공연장Id, 변경할 주소를 입력받아 공연장의 주소를 변경한다.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{venueId}")
    public ResponseEntity<Void> changeAddress(@PathVariable Long venueId,
                                              @Valid @RequestBody ChangeAddressRequest request) {
        changeVenueService.changeAddress(venueId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "활성화 된 공연장 목록을 조회", description = "공연장Id, 장소명, 수용 인원, 주소, 상태 리스트를 반환한다.")
    @GetMapping
    public ResponseEntity<List<VenueData>> getActivateVenueList() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(venueQueryService.getActivateVenueList());
    }
}
