package com.tae.Etickette.booking.domain;

import com.tae.Etickette.global.model.Seat;
import com.tae.Etickette.session.domain.Session;

import java.util.List;

/**
 * Booking 의 공통된 로직을 구분한 헬퍼 클래스
 */
public final class BookingServiceHelper {

    /**
     * 이미 예약된 좌석인지 확인한다.
     * @param session : 예약을 진행하는 세션
     * @param bookedSeats : 예약된 좌석 목록
     * @param requestSeats : 예약 요청 좌석 목록
     */
    public static void verifySeatsNotAlreadyBooked(Session session, List<Seat> bookedSeats, List<Seat> requestSeats) {
        //유효한 좌석인 지 검증
        session.verifySeatsExist(requestSeats);


        for (Seat requestedSeat : requestSeats) {
            if (bookedSeats.contains(requestedSeat)) {
                throw new AlreadyBookedException("이미 예약된 좌석입니다.");
            }
        }
    }

    /**
     * 좌석의 정보를 매칭한다.
     * @param requestSeats : 예약 요청 좌석 목록
     * @param seatingPlan : 세션의 좌석 목록
     * @return : 세션의 좌석 정보를 예약 좌석에 등록
     */
    public static List<Seat> registerSeatInfo(
            List<Seat> requestSeats,
            List<Seat> seatingPlan) {
        return requestSeats.stream()
                .map(request -> {
                    Seat matched = seatingPlan.stream()
                            .filter(s -> s.equals(request))  // row & column 비교
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("좌석 정보 불일치"));
                    return new Seat(request.getRowNum(), request.getColumnNum(), matched.getGrade(), matched.getPrice());
                })
                .toList();
    }
}
