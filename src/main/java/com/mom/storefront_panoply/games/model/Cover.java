package com.mom.storefront_panoply.games.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cover {
    private long id;
    private String url;
    @JsonProperty("image_id")
    private String imageId;
}
