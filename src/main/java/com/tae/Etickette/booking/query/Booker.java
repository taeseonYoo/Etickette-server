package com.tae.Etickette.booking.query;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Booker {
    private final String name;
    private final String email;
    public Booker(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
