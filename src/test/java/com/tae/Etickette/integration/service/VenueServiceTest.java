package com.tae.Etickette.integration.service;

import com.tae.Etickette.concert.application.Dto.VenueCreateRequestDto;
import com.tae.Etickette.concert.application.VenueService;
import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.concert.domain.Section;
import com.tae.Etickette.concert.domain.Venue;
import com.tae.Etickette.concert.infra.SectionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.tae.Etickette.concert.application.Dto.VenueCreateRequestDto.builder;

@SpringBootTest
@Transactional
public class VenueServiceTest {
    @Autowired
    private VenueService venueService;
    @Autowired
    private SectionRepository sectionRepository;

    @Test
    public void 성공() {

        //given
        List<VenueCreateRequestDto.SectionRequestDto> sections = new ArrayList<>();
        sections.add(VenueCreateRequestDto.SectionRequestDto.builder().grade("VIP").price(150000).build());
        sections.add(VenueCreateRequestDto.SectionRequestDto.builder().grade("A").price(100000).build());
        sections.add(VenueCreateRequestDto.SectionRequestDto.builder().grade("R").price(50000).build());

        VenueCreateRequestDto requestDto = builder()
                .place("KSPO DOME(올림픽 체조경기장)")
                .capacity(15000)
                .address(new Address("서울시", "송파구 올림픽로 424", "11111"))
                .sections(sections)
                .build();

        venueService.createVenue(requestDto);

        List<Section> all = sectionRepository.findAll();
        for (Section section : all) {
            System.out.println(section.getGrade() + " " + section.getPrice() + " " + section.getVenue());
        }
        Assertions.assertThat(all.size()).isEqualTo(3);
    }
}
