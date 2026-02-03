package com.mom.storefront_panoply.games.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Platform {
    private Long id;
    private String name;
    private String slug;
    private String abbreviation;
    private PlatformCategory category;
    private String summary;
}