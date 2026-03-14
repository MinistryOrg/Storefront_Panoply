package com.mom.storefront_panoply.games.model.dto;

import com.mom.storefront_panoply.games.model.dbo.GameEntity;
import com.mom.storefront_panoply.igdb.model.*;
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
    private GameStatus gameStatus;
    private LocalDateTime firstReleaseDate;
    private GameDto versionParent;

}
