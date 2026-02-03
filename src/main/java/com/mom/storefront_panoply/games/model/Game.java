package com.mom.storefront_panoply.games.model;

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
    private Double totalRatingCount;

    private String status;

    @JsonProperty("aggregated_rating")
    private Double aggregatedRating;

    @JsonProperty("aggregated_rating_count")
    private Double aggregatedRatingCount;

    private Double rating;

    private Cover cover;

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

    private List<Game> ports;
    private List<Game> remakes;
    private List<Game> remasters;

    @JsonProperty("version_title")
    private String versionTitle;

    @JsonProperty("parent_game")
    private Game parentGame;

    private List<Game> dlcs;
    private List<Game> expansions;

    @JsonProperty("standalone_expansions")
    private List<Game> standaloneExpansions;

    private List<Game> bundles;

    private List<Screenshot> screenshots;
    private List<Video> videos;
    private List<Artwork> artworks;

    @JsonProperty("alternative_names")
    private List<AlternativeName> alternativeNames;

    @JsonProperty("external_games")
    private List<ExternalGame> externalGames;

    private List<Website> websites;
    private List<Keyword> keywords;
}
