package com.tae.Etickette.bookseat.presentation;

import com.tae.Etickette.bookseat.application.CancelBookSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BookSeatController {
    private final CancelBookSeatService cancelBookSeatService;

    public void cancel() {
//        SecurityContextHolder.getContext().getAuthentication()
//        cancelBookSeatService.cancel();
    }
}
