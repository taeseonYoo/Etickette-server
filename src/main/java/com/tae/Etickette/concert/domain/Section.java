package com.tae.Etickette.concert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;
    private String grade;
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;


    private Section(String grade, Integer price) {
        this.grade = grade;
        this.price = price;
    }

    public static Section create(String grade, Integer price) {
        return new Section(grade, price);
    }
    public void addConcert(Concert concert) {
        this.concert = concert;
    }
}
