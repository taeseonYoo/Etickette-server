package com.tae.Etickette.concert.presentation;

import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertResponse;
import com.tae.Etickette.concert.query.application.ConcertDetail;
import com.tae.Etickette.concert.query.application.ConcertDetailService;
import com.tae.Etickette.concert.query.application.ConcertSummaryService;
import com.tae.Etickette.concert.query.dto.ConcertSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concerts")
public class ConcertController {
    private final RegisterConcertService registerConcertService;
    private final ConcertSummaryService concertSummaryService;
    private final ConcertDetailService concertDetailService;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisterConcertResponse> register(
            @RequestPart("request") RegisterConcertRequest request,
            @RequestPart("image") MultipartFile image) {

        return new ResponseEntity<>(registerConcertService.register(request, image), HttpStatus.CREATED);
    }

    /**
     * 공연 요약 정보 리스트 + 페이징
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ConcertSummary>> getConcertSummaries(@PageableDefault(size = 10) Pageable pageable) {
        List<ConcertSummary> summaries = concertSummaryService.getPageList(pageable);
        return ResponseEntity.ok(summaries);
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
