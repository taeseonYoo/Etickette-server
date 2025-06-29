package com.tae.Etickette.venue.command.domain;

import com.tae.Etickette.concert.command.domain.Address;
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
    @Enumerated(value = EnumType.STRING)
    private VenueStatus status;

    private Venue(String place, Integer capacity,Address address) {
        this.place = place;
        setCapacity(capacity);
        this.address = address;
        this.status = VenueStatus.ACTIVE;
    }
    public static Venue create(String place, Integer capacity,Address address) {
        return new Venue(place, capacity,address);
    }
    public void changeAddress(Address newAddress) {
        verifyNotYetDelete();
        this.address = newAddress;
    }
    public void deleteVenue() {
        verifyNotYetDelete();
        this.status = VenueStatus.DELETED;
    }
    private void verifyNotYetDelete() {
        if (!isNotDelete()) {
            throw new AlreadyDeletedException("이미 삭제된 공연장 입니다.");
        }
    }
    private boolean isNotDelete() {
        return status == VenueStatus.ACTIVE;
    }

    public void changeCapacity(Integer capacity) {
        verifyNotYetDelete();
        setCapacity(capacity);
    }
    private void verifyCapacityIsNotNegative(Integer capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("총 좌석 수는 0보다 작을 수 없습니다.");
        }
    }

    private void setCapacity(Integer capacity) {
        verifyCapacityIsNotNegative(capacity);
        this.capacity = capacity;
    }
}
