package com.tae.Etickette.concert.presentation;

import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.query.application.ConcertDetail;
import com.tae.Etickette.concert.query.application.ConcertDetailService;
import com.tae.Etickette.concert.query.application.ConcertSummaryService;
import com.tae.Etickette.concert.query.dto.ConcertSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concerts")
public class ConcertController {
    private final RegisterConcertService registerConcertService;
    private final ConcertSummaryService concertSummaryService;
    private final ConcertDetailService concertDetailService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> register(@RequestBody RegisterConcertRequest request) {
        registerConcertService.register(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 공연 요약 정보 리스트 + 페이징
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<PagedModel<ConcertSummary>> getConcertSummaries(Pageable pageable) {
        Page<ConcertSummary> summaries = concertSummaryService.getPageList(pageable);
        return ResponseEntity.ok(new PagedModel<>(summaries));
    }

    /**
     * 공연 상세 정보
     * @param concertId
     * @return
     */
    @GetMapping("/{concertId}")
    public ResponseEntity<ConcertDetail> concertDetail(@PathVariable Long concertId) {
        return concertDetailService.getConcertDetail(concertId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
