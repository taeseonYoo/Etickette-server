package com.tae.Etickette.bookseat.command.domain;

import java.util.List;

public interface CustomBookSeatRepository {
    void saveAllInBulk(List<BookSeat> bookSeats);
}
