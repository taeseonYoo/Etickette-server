package com.tae.Etickette.session.domain;

import com.tae.Etickette.concert.domain.GradePrice;
import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.global.model.Seat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SettingSeatService {

    public List<Seat> setting(List<GradePrice> gradePrices) {
        List<Seat> seatingPlan = new ArrayList<>();

        Map<String, Money> map = gradePrices.stream()
                .collect(Collectors.toMap(GradePrice::getGrade, GradePrice::getPrice));

        for (int i = 1; i <= 50; i++) {
            seatingPlan.add(new Seat(String.valueOf((char) ('A' + (i - 1) / 10)), ((i - 1) % 10) + 1, "VIP", map.getOrDefault("VIP", null)));
        }
        for (int i = 1; i <= 50; i++) {
            seatingPlan.add(new Seat(String.valueOf((char) ('F' + (i - 1) / 10)), ((i - 1) % 10) + 1, "S", map.getOrDefault("S", null)));
        }

        return seatingPlan;
    }
}
