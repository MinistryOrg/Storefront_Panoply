package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Franchise {

    private Long id;
    private String name;
    private String url;

    private List<Game> games;

    public Franchise(Long id) {
        this.id = id;
    }
}
