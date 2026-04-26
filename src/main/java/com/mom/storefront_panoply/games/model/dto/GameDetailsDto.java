package com.mom.storefront_panoply.games.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mom.storefront_panoply.games.model.dbo.FranchiseEntity;
import com.mom.storefront_panoply.games.model.dbo.FranchiseRef;
import com.mom.storefront_panoply.games.model.dbo.GenreEntity;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameDetailsDto {
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

    private GameStatus gameStatus;

    private Double aggregatedRating;

    private Integer aggregatedRatingCount;

    private Double rating;

    private Integer hypes;

    private Cover cover;

    /* ===================== Relations ===================== */

    private List<GameDetailsDto> similarGames;

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

    /* ===================== Versions ===================== */

    private List<GameDetailsDto> ports;
    private List<GameDetailsDto> remakes;
    private List<GameDetailsDto> remasters;

    private String versionTitle;

    private GameDetailsDto versionParent;

    private GameDetailsDto parentGame;

    /* ===================== DLC ===================== */

    private List<GameDetailsDto> dlcs;
    private List<GameDetailsDto> expansions;

    private List<GameDetailsDto> standaloneExpansions;

    private List<GameDetailsDto> bundles;

    /* ===================== Franchise / Collection ===================== */

    private FranchiseDto franchise;

    private List<FranchiseDto> franchises;

    private List<CollectionDto> collections;

    private Long type;

    /* ===================== Media ===================== */

    private List<Screenshot> screenshots;
    private List<Video> videos;
    private List<Artwork> artworks;

    /* ===================== Metadata ===================== */

    private List<AlternativeName> alternativeNames;

    private List<ExternalGame> externalGames;

    private List<Website> websites;

    private List<Keyword> keywords;

    private GameTimeToBeats timeToBeat;

    private Boolean isPopular;
}
