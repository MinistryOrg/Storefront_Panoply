package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgeRating {
    private Long id;
    @JsonProperty("rating_category")
    private RatingCategory ratingCategory;
    @JsonProperty("rating_content_descriptions")
    private List<RatingContentDescriptions> ratingContentDescriptions;
    private Organization organization;
    private String category;
    private String synopsis;

    public AgeRating(Long id) {
        this.id = id;
    }
}
