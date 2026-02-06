package com.mom.storefront_panoply.games.model.dbo;

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

    /* ===================== Relations ===================== */

    private List<Game> similarGames;

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

    private List<Game> ports;
    private List<Game> remakes;
    private List<Game> remasters;

    private String versionTitle;

    private Game versionParent;

    private Game parentGame;

    /* ===================== DLC ===================== */

    private List<Game> dlcs;
    private List<Game> expansions;

    private List<Game> standaloneExpansions;

    private List<Game> bundles;

    /* ===================== Franchise / Collection ===================== */

    private Franchise franchise;

    private List<FranchiseEntity> franchises;

    private List<Collection> collections;

    private Integer gameType;

    /* ===================== Media ===================== */

    private List<Screenshot> screenshots;
    private List<Video> videos;
    private List<Artwork> artworks;

    /* ===================== Metadata ===================== */

    private List<AlternativeName> alternativeNames;

    private List<ExternalGame> externalGames;

    private List<Website> websites;

    private List<Keyword> keywords;

    private Boolean isPopular;
}
