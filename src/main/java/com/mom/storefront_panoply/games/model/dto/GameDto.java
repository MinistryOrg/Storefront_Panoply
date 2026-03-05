package com.mom.storefront_panoply.games.model.dto;

import com.mom.storefront_panoply.igdb.model.GameType;
import com.mom.storefront_panoply.igdb.model.Platform;
import com.mom.storefront_panoply.igdb.model.Video;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private List<Video> video;
    private List<Platform> platforms;
    private Long type;
    private LocalDateTime firstReleaseDate;
}
