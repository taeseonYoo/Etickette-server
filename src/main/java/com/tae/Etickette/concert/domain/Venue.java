package com.tae.Etickette.concert.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany(mappedBy = "venue")
    private List<Concert> concerts = new ArrayList<>();

    private Venue(String place, Integer capacity,Address address) {
        this.place = place;
        this.capacity = capacity;
        this.address = address;
    }
    public static Venue create(String place, Integer capacity,Address address) {
        return new Venue(place, capacity,address);
    }
    public void deleteVenue() {
        this.status = VenueStatus.DELETE;
    }
    public void updateCapacity(Integer capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("총 좌석 수는 0보다 작을 수 없습니다.");
        }
        this.capacity = capacity;
    }

    public void changeAddress(Address newAddress) {
        verifyNotDelete();
        setAddress(newAddress);
    }
    private void setAddress(Address newAddress) {
        if (newAddress == null) {
            throw new IllegalArgumentException("no address");
        }
        this.address = newAddress;
    }
    private void verifyNotDelete() {
        if (status == VenueStatus.DELETE) {
            throw new VenueAlreadyDeletedException("삭제된 공연장의 주소는 수정할 수 없습니다.");
        }
    }



}
