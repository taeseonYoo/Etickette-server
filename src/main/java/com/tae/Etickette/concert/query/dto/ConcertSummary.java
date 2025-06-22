package com.tae.Etickette.concert.query.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;


@Entity
@Immutable
@Synchronize({"concert","session","venue"})
@Subselect("""
SELECT DISTINCT 
    c.concert_id,c.title, c.imgURL, v.place
FROM concert c join venue v on v.venue_id = c.venue_id
""")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSummary {
    @Id
    private Long concertId;
    private String title;
    private String imgURL;
    private String place;
}
