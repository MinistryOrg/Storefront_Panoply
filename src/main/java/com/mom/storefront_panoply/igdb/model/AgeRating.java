package com.mom.storefront_panoply.igdb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AgeRating {
    private Long id;
    private Integer rating;
    private Integer organization;
    private String category;
    private String synopsis;
}
