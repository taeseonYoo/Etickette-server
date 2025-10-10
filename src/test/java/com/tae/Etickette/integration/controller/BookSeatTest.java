package com.tae.Etickette.integration.controller;

import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.global.model.Money;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration - BookSeat")
public class BookSeatTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookSeatRepository bookSeatRepository;

    @Test
    @DisplayName("/api/seats/{sessionId} - 좌석 정보 조회에 성공한다.")
    void getSeatInfo_success() throws Exception {
        //given
        bookSeatRepository.save(BookSeat.create(1L, 1L, "VIP", new Money(10000)));
        bookSeatRepository.save(BookSeat.create(2L, 1L, "R", new Money(5000)));
        //when & then
        mockMvc.perform(get("/api/seats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                // VIP 좌석 (첫 번째 요소) 상세 검증
                .andExpect(jsonPath("$[0].grade").value("VIP"))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
                // R 좌석 (두 번째 요소) 상세 검증
                .andExpect(jsonPath("$[1].grade").value("R"))
                .andExpect(jsonPath("$[1].status").value("AVAILABLE"));
    }
    @Test
    @DisplayName("/api/seats/{sessionId} - 좌석이 없을 경우 빈 리스트 []와 200 OK를 반환한다.")
    void getSeatInfo_empty_list() throws Exception {
        Long nonExistentSessionId = 999L;

        //given 데이터베이스에 999L 세션 ID에 해당하는 좌석은 없음.

        //when & then
        mockMvc.perform(get("/api/seats/" + nonExistentSessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isEmpty()); // JSON 배열이 비어있는지 검증
    }
    @Test
    @DisplayName("/api/seats/{sessionId} - 유효하지 않은 PathVariable에 대해 400 Bad Request를 반환한다.")
    void getSeatInfo_invalid_PathVariable() throws Exception {
        //given: 세션 ID에 숫자가 아닌 문자열을 전달

        //when & then
        mockMvc.perform(get("/api/seats/invalid_id"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.code").value("VALID-001"))
                .andExpect(jsonPath("$.message", Matchers.containsString("sessionId")));
    }
}
