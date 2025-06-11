package com.tae.Etickette.venue.presentation;

import com.tae.Etickette.venue.application.Dto.DeleteVenueRequest;
import com.tae.Etickette.venue.application.DeleteVenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VenueController {

    private final DeleteVenueService deleteVenueService;
    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/")
    public void delete(@Valid DeleteVenueRequest requestDto) {
        deleteVenueService.delete(requestDto);
    }
}
