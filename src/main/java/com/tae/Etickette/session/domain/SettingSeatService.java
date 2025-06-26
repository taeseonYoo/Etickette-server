package com.tae.Etickette.session.domain;

import com.tae.Etickette.concert.command.domain.GradePrice;
import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.bookseat.command.domain.BookSeat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SettingSeatService {

    public List<BookSeat> setting(List<Long> seatIds, List<GradePrice> gradePrices, Long sessionId) {

        List<BookSeat> bookSeats = new ArrayList<>();

        Map<String, Money> map = gradePrices.stream()
                .collect(Collectors.toMap(GradePrice::getGrade, GradePrice::getPrice));

        int i = 0;
        String grade = "";

        for (Long seatId : seatIds) {
            if (i < 50) grade = "VIP";
            else if (i < 100) grade = "S";
            else grade = "R";
            bookSeats.add(BookSeat.create(seatId, sessionId, grade, map.getOrDefault(grade, null)));
            i++;
        }
        return bookSeats;
    }
}
