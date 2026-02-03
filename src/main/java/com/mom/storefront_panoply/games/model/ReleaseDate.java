package com.mom.storefront_panoply.games.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReleaseDate {
    private long id;
    private long date; // timestamp
    private String human;
    private Platform platform;
    private String region;
    private Integer category;
}