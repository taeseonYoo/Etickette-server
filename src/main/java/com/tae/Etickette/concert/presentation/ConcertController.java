package com.tae.Etickette.concert.presentation;

import com.tae.Etickette.concert.application.RegisterConcertService;
import com.tae.Etickette.concert.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.query.application.ConcertQueryService;
import com.tae.Etickette.concert.query.dto.ConcertSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concerts")
public class ConcertController {
    private final RegisterConcertService registerConcertService;
    private final ConcertQueryService concertQueryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> register(RegisterConcertRequest request) {
        registerConcertService.register(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedModel<ConcertSummary>> getConcertSummaries(Pageable pageable) {
        Page<ConcertSummary> allConcerts = concertQueryService.getPageList(pageable);
        return ResponseEntity.ok(new PagedModel<>(allConcerts));
    }
}
