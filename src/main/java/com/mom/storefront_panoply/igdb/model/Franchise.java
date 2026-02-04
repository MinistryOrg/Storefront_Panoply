package com.mom.storefront_panoply.igdb.model;

import lombok.Data;

import java.util.List;

@Data
public class Franchise {

    private Long id;
    private String name;
    private String url;

    private List<Game> games;
}
