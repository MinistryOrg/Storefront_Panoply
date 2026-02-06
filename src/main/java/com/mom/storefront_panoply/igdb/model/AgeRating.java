package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgeRating {
    private Long id;
    private Integer rating;
    private Organization organization;
    private String category;
    private String synopsis;

    public AgeRating(Long id) {
        this.id = id;
    }
}
