package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    private Long id;
    private String name;
    private String summary;
    private String storyline;

    @JsonProperty("first_release_date")
    private Date firstReleaseDate;

    @JsonProperty("total_rating")
    private Double totalRating;

    @JsonProperty("total_rating_count")
    private Integer totalRatingCount;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    private Integer status;

    @JsonProperty("aggregated_rating")
    private Double aggregatedRating;

    @JsonProperty("aggregated_rating_count")
    private Integer aggregatedRatingCount;

    private Double rating;

    private Integer hypes;

    private Cover cover;

    /* ===================== Relations ===================== */

    @JsonProperty("similar_games")
    private List<Game> similarGames;

    private List<Platform> platforms;

    private List<Genre> genres;
    private List<Theme> themes;

    @JsonProperty("game_modes")
    private List<GameMode> gameModes;

    @JsonProperty("player_perspectives")
    private List<PlayerPerspective> playerPerspectives;

    @JsonProperty("age_ratings")
    private List<AgeRating> ageRatings;

    @JsonProperty("involved_companies")
    private List<InvolvedCompany> involvedCompanies;

    @JsonProperty("game_engines")
    private List<GameEngine> gameEngines;

    @JsonProperty("game_localizations")
    private List<GameLocalization> gameLocalizations;

    @JsonProperty("language_supports")
    private List<LanguageSupport> languageSupports;

    /* ===================== Versions ===================== */

    private List<Game> ports;
    private List<Game> remakes;
    private List<Game> remasters;

    @JsonProperty("version_title")
    private String versionTitle;

    @JsonProperty("version_parent")
    private Game versionParent;

    @JsonProperty("parent_game")
    private Game parentGame;

    /* ===================== DLC ===================== */

    private List<Game> dlcs;
    private List<Game> expansions;

    @JsonProperty("standalone_expansions")
    private List<Game> standaloneExpansions;

    private List<Game> bundles;

    /* ===================== Franchise / Collection ===================== */

    private Franchise franchise;

    private List<Franchise> franchises;

    private List<Collection> collections;

    @JsonProperty("game_type")
    private Integer gameType;

    /* ===================== Media ===================== */

    private List<Screenshot> screenshots;
    private List<Video> videos;
    private List<Artwork> artworks;

    /* ===================== Metadata ===================== */

    @JsonProperty("alternative_names")
    private List<AlternativeName> alternativeNames;

    @JsonProperty("external_games")
    private List<ExternalGame> externalGames;

    private List<Website> websites;

    private List<Keyword> keywords;
}
