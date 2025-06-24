package com.tae.Etickette.bookseat.application;

import com.tae.Etickette.bookseat.domain.BookSeat;
import com.tae.Etickette.bookseat.domain.BookSeatId;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookSeatService {
    private final BookSeatRepository bookSeatRepository;

    public void book(BookSeatId id) {
        BookSeat bookSeat = bookSeatRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(""));

        bookSeat.reserve();
    }
}
