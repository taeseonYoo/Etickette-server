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

    @GetMapping("/{sessionId}")
    public ResponseEntity<List<BookSeatData>> getSeatInfo(@PathVariable Long sessionId) {
        return ResponseEntity.ok(bookSeatQueryService.getAllSeat(sessionId));
    }
}
