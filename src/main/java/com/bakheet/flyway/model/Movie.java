package com.bakheet.flyway.model;


import com.bakheet.flyway.model.enums.ContentRating;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String headline;
    private String thumbnail;
    private String language;
    private String region;

    @Enumerated(EnumType.STRING)
    private ContentRating rating;

    @ManyToMany
    Set<Actor> actors;
}
