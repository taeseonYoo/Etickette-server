package com.tae.Etickette.bookseat.presentation;

import com.tae.Etickette.bookseat.query.BookSeatData;
import com.tae.Etickette.bookseat.query.BookSeatQueryService;
import com.tae.Etickette.global.api.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "BookSeat API",description = "예약 좌석 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seats")
public class BookSeatController {
    private final BookSeatQueryService bookSeatQueryService;

    @Operation(summary = "공연의 좌석 예약 상태를 조회한다.",description = "[좌석 번호, 스케줄 ID, 좌석 등급, 좌석 상태] 리스트를 반환한다.")
    @GetMapping("/{sessionId}")
    public ResponseEntity<SuccessResponse<List<BookSeatData>>> getSeatInfo(@PathVariable Long sessionId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.success(bookSeatQueryService.getAllSeat(sessionId)));
    }
}
