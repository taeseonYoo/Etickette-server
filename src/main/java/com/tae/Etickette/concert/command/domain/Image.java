package com.tae.Etickette.concert.command.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Column(name = "image_path")
    private String path;

    public Image(String path) {
        this.path = path;
    }
}
