package com.mom.storefront_panoply.games.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {
    private Long id;
    private String name;
    private String summary;
    private String coverImageId;
    private String totalRating;
    private String rating;
    private List<String> screenshotsImageId;
    private List<String> genreName;
}
