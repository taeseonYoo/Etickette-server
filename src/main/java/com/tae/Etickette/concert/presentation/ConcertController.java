package com.tae.Etickette.concert.presentation;

import com.tae.Etickette.concert.application.RegisterConcertService;
import com.tae.Etickette.concert.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.query.application.ConcertDetail;
import com.tae.Etickette.concert.query.application.ConcertDetailService;
import com.tae.Etickette.concert.query.application.ConcertQueryService;
import com.tae.Etickette.concert.query.dto.ConcertSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concerts")
public class ConcertController {
    private final RegisterConcertService registerConcertService;
    private final ConcertQueryService concertQueryService;
    private final ConcertDetailService concertDetailService;
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

    @GetMapping("/{concertId}")
    public ResponseEntity<ConcertDetail> concertDetail(@PathVariable Long concertId) {
        Optional<ConcertDetail> concertDetail = concertDetailService.getConcertDetail(concertId);

        if (concertDetail.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(concertDetail.get());
        }
    }
}
