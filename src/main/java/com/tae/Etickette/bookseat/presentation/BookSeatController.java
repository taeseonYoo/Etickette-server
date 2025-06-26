package com.tae.Etickette.bookseat.presentation;

import com.tae.Etickette.bookseat.query.BookSeatData;
import com.tae.Etickette.bookseat.query.BookSeatQueryService;
import com.tae.Etickette.bookseat.command.application.CancelBookSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats")
public class BookSeatController {
    private final CancelBookSeatService cancelBookSeatService;
    private final BookSeatQueryService bookSeatQueryService;

    public void cancel() {
//        SecurityContextHolder.getContext().getAuthentication()
//        cancelBookSeatService.cancel();
    }
    @GetMapping("/{sessionId}")
    public ResponseEntity<List<BookSeatData>> getSeatInfo(@PathVariable Long sessionId) {
        return ResponseEntity.ok(bookSeatQueryService.getAllSeat(sessionId));
    }
}
