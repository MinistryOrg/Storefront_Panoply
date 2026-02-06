package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Screenshot {
    private Long id;
    @JsonProperty("image_id")
    private String imageId;
    private String url;

    public Screenshot(Long id) {
        this.id = id;
    }
}

