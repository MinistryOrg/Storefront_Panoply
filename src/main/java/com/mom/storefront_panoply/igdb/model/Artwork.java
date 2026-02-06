package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Artwork {
    private Long id;
    private String image_id;
    private String artwork_type;

    public Artwork(Long id) {
        this.id = id;
    }
}
