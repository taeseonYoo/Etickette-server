package com.tae.Etickette.concert.entity;

import jakarta.persistence.*;
import lombok.*;

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

    public void updateCapacity(Integer capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("총 좌석 수는 0보다 작을 수 없습니다.");
        }
        this.capacity = capacity;
    }

    public void updateAddress(Address address) {
        this.address = address;
    }

    private Venue(String place, Integer capacity,Address address) {
        this.place = place;
        this.capacity = capacity;
        this.address = address;
    }
    public static Venue create(String place, Integer capacity,Address address) {
        return new Venue(place, capacity,address);
    }
}
