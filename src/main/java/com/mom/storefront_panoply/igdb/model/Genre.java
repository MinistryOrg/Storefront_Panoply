package com.mom.storefront_panoply.igdb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    private Long id;
    private String name;
    private String slug;

    public Genre(Long id) {
        this.id = id;
    }
}

