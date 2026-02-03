package com.mom.storefront_panoply.games.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Screenshot {
    private Long id;
    @JsonProperty("image_id")
    private String imageId;
    private String url;
}

