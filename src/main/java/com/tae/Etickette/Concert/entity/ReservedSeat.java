package com.tae.Etickette.Concert.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ReservedSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String row;
    @Column
    private Integer column;

    @ManyToOne
    @JoinColumn
    private Schedule schedule;
    @ManyToOne
    @JoinColumn
    private Section section;
}
