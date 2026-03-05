package com.mom.storefront_panoply.igdb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingCategory {
    private Long id;
    private String rating;

    public RatingCategory(Long id) {
        this.id = id;
    }
}
