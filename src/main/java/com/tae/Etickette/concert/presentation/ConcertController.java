package com.tae.Etickette.concert.presentation;

import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertResponse;
import com.tae.Etickette.concert.query.application.ConcertDetail;
import com.tae.Etickette.concert.query.application.ConcertDetailService;
import com.tae.Etickette.concert.query.application.ConcertSummaryService;
import com.tae.Etickette.concert.query.dto.ConcertSummary;
import com.tae.Etickette.global.api.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "Concert API", description = "콘서트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
public class ConcertController {
    private final RegisterConcertService registerConcertService;
    private final ConcertSummaryService concertSummaryService;
    private final ConcertDetailService concertDetailService;

    @Operation(summary = "공연 등록",description = "제목, 설명, 러닝타임, 좌석 가격, 포스터를 입력하여 공연장을 등록한다.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<RegisterConcertResponse>> register(
            @RequestPart("request") RegisterConcertRequest request,
            @RequestPart("image") MultipartFile image) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.success(registerConcertService.register(request, image)));
    }

    @Operation(summary = "공연 요약 정보 조회", description = "페이징 된 [공연장Id, 제목, 이미지, 장소명] 리스트를 반환한다.")
    @GetMapping
    public ResponseEntity<SuccessResponse<List<ConcertSummary>>> getConcertSummaries(@PageableDefault(size = 10) Pageable pageable) {
        List<ConcertSummary> summaries = concertSummaryService.getPageList(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.success(summaries));
    }

    @Operation(summary = "공연 상세 정보 조회",description = "공연Id로 공연의 상세 정보를 조회하여 반환한다.")
    @GetMapping("/{concertId}")
    public ResponseEntity<SuccessResponse<ConcertDetail>> concertDetail(@PathVariable Long concertId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.success(concertDetailService.getConcertDetail(concertId)));
    }
}
