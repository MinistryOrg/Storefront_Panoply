package com.mom.storefront_panoply.games.model.dto;

import org.springframework.data.domain.Sort;

public enum GameSort {
    NAME_ASC,
    NAME_DESC,
    RATING_ASC,
    RATING_DESC,
    POPULAR,
    HYPE;


    public Sort toSpringSort() {
        return switch (this) {
            case NAME_DESC -> Sort.by(Sort.Direction.DESC, "name");
            // wrong 
            case POPULAR -> Sort.by(Sort.Direction.DESC, "totalRating", "totalRatingCount");

            case HYPE -> Sort.by(Sort.Direction.DESC, "hypes");

            case NAME_ASC -> Sort.by(Sort.Direction.ASC, "name");

            case RATING_ASC -> Sort.by(Sort.Direction.ASC, "rating");

            case RATING_DESC -> Sort.by(Sort.Direction.DESC, "rating");
        };
    }
}
