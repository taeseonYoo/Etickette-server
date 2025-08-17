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

    //TODO 쿼리 튜닝 대상, 현재는 많은 데이터를 포함하지만, 좌석 id와 상태면 충분하다.
    @GetMapping("/{sessionId}")
    public ResponseEntity<List<BookSeatData>> getSeatInfo(@PathVariable Long sessionId) {
        return ResponseEntity.ok(bookSeatQueryService.getAllSeat(sessionId));
    }
}
