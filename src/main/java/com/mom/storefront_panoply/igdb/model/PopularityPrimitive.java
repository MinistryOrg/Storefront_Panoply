package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PopularityPrimitive {
    private Long id;
    @JsonProperty("game_id")
    private Long gameId;
    private Double value;
    @JsonProperty("popularity_type")
    private Integer popularityType;
}
