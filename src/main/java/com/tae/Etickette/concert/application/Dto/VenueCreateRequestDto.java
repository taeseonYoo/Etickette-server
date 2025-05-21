package com.tae.Etickette.concert.application.Dto;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.concert.domain.Section;
import com.tae.Etickette.concert.domain.Venue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class VenueCreateRequestDto {
    @NotBlank
    private final String place;
    @Size(min = 1)
    private final Integer capacity;
    @NotBlank
    private final Address address;

    private final List<SectionRequestDto> sections;

    @Builder
    public VenueCreateRequestDto(String place, Integer capacity, Address address, List<SectionRequestDto> sections) {
        this.place = place;
        this.capacity = capacity;
        this.address = address;
        this.sections = sections;
    }

    public Venue toEntity() {
        Venue venue = Venue.create(place, capacity, address);

        for (SectionRequestDto section : sections) {
            Section entity = Section.create(section.grade, section.price);
            venue.addSection(entity);
        }

        return venue;
    }

    @Getter
    public static class SectionRequestDto{
        private String grade;
        private Integer price;
        @Builder
        public SectionRequestDto(String grade, Integer price) {
            this.grade = grade;
            this.price = price;
        }
    }
}
