package com.mom.storefront_panoply.games.model.dbo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mom.storefront_panoply.igdb.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameEntity {
    @Id
    private String id;
    private String name;
    private String summary;
    private String storyline;

    private LocalDateTime firstReleaseDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Double totalRating;

    private Integer totalRatingCount;

    private Integer status;

    private Double aggregatedRating;

    private Integer aggregatedRatingCount;

    private Double rating;

    private Integer hypes;

    private Cover cover;

    private Double popularity;

    private GameStatus gameStatus;

    // Relations

    private List<GameEntity> similarGames;

    private List<Platform> platforms;

    private List<GenreEntity> genres;
    private List<Theme> themes;

    private List<GameMode> gameModes;

    private List<PlayerPerspective> playerPerspectives;

    private List<AgeRating> ageRatings;

    private List<InvolvedCompany> involvedCompanies;

    private List<GameEngine> gameEngines;

    private List<GameLocalization> gameLocalizations;

    private List<LanguageSupport> languageSupports;

    // Versions

    private List<GameEntity> ports;
    private List<GameEntity> remakes;
    private List<GameEntity> remasters;

    private String versionTitle;

    private GameEntity versionParent;

    private GameEntity parentGame;

    // DLC

    private List<GameEntity> dlcs;
    private List<GameEntity> expansions;

    private List<GameEntity> standaloneExpansions;

    private List<GameEntity> bundles;

    // Franchise / Collection

    private FranchiseEntity franchise;

    private List<FranchiseEntity> franchises;

    private List<CollectionEntity> collections;

    private Long type;

    // Media

    private List<Screenshot> screenshots;
    private List<Video> videos;
    private List<Artwork> artworks;

    // Metadata

    private List<AlternativeName> alternativeNames;

    private List<ExternalGame> externalGames;

    private List<Website> websites;

    private List<Keyword> keywords;

    private Boolean isPopular;
}
