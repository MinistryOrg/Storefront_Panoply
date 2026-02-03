package com.mom.storefront_panoply.games.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Esrb {
    private long id;
    private String rating;
    private String synopsis;
    private String name;
}