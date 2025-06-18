package com.tae.Etickette.venue.query;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.command.domain.VenueStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "venue")
public class VenueData {
    @Id
    @Column(name = "venue_id")
    private Long id;
    @Column
    private String place;
    @Column(nullable = false)
    private Integer capacity;
    @Embedded
    private Address address;
    @Enumerated(value = EnumType.STRING)
    private VenueStatus status;
}
