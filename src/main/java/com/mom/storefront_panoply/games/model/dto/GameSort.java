package com.mom.storefront_panoply.games.model.dto;

import org.springframework.data.domain.Sort;

public enum GameSort {
    NAME_ASC,
    NAME_DESC,
    POPULAR,
    HYPE;

    public static GameSort from(String value) {
        if (value == null) return NAME_ASC;

        return switch (value.toLowerCase()) {
            case "name_desc", "z-a" -> NAME_DESC;
            case "popular" -> POPULAR;
            case "hype" -> HYPE;
            default -> NAME_ASC;
        };
    }

    public Sort toSpringSort() {
        return switch (this) {
            case NAME_DESC ->
                    Sort.by(Sort.Direction.DESC, "name");

            case POPULAR ->
                    Sort.by(Sort.Direction.DESC, "totalRating", "totalRatingCount");

            case HYPE ->
                    Sort.by(Sort.Direction.DESC, "hypes");

            case NAME_ASC ->
                    Sort.by(Sort.Direction.ASC, "name");
        };
    }
}
