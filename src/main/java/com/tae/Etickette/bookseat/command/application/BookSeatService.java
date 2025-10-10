package com.tae.Etickette.bookseat.command.application;

import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookSeatService {
    private final BookSeatRepository bookSeatRepository;
    @Transactional
    public void book(BookSeatId id) {
        BookSeat bookSeat = bookSeatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKSEAT_NOT_FOUND, "예매 좌석을 찾을 수 없습니다."));

        bookSeat.reserve();
    }
}
