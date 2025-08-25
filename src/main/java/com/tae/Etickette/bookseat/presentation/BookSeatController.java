package com.tae.Etickette.bookseat.presentation;

import com.tae.Etickette.bookseat.query.BookSeatData;
import com.tae.Etickette.bookseat.query.BookSeatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats")
public class BookSeatController {
    private final BookSeatQueryService bookSeatQueryService;

    /**
     * 좌석 정보 조회 요청
     * 좌석 정보 요청만 사용하고 트랜잭션을 사용하지 않으므로 Repo에서 직접 호출하는 방식의 개선이 가능하다.
     * @param sessionId
     * @return
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<List<BookSeatData>> getSeatInfo(@PathVariable Long sessionId) {
        return ResponseEntity.ok(bookSeatQueryService.getAllSeat(sessionId));
    }
}
