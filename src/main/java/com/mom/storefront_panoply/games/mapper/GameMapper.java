package com.mom.storefront_panoply.games.mapper;

import com.mom.storefront_panoply.games.model.dbo.*;
import com.mom.storefront_panoply.games.model.dto.GameDetailsDto;
import com.mom.storefront_panoply.games.model.dto.GameDto;
import com.mom.storefront_panoply.igdb.model.*;
import com.mom.storefront_panoply.tools.Util;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class GameMapper {

    public GameDto toGameDto(GameEntity entity) {

        return GameDto.builder()
                .id(Long.valueOf(entity.getId()))
                .name(entity.getName())
                .summary(entity.getSummary())

                // Cover
                .coverImageId(
                        entity.getCover() != null
                                ? entity.getCover().getImageId()
                                : null
                )

                // Ratings
                .totalRating(
                        entity.getTotalRating() != null
                                ? entity.getTotalRating().toString()
                                : null
                )

                .rating(
                        entity.getRating() != null
                                ? entity.getRating().toString()
                                : null
                )

                // Screenshot
                .screenshotsImageId(
                        entity.getScreenshots() != null &&
                                !entity.getScreenshots().isEmpty()
                                ? entity.getScreenshots().stream()
                                .map(Screenshot::getImageId)
                                .toList()
                                : null
                )

                // Genres
                .genreName(
                        entity.getGenres() != null
                                ? entity.getGenres()
                                .stream()
                                .map(GenreEntity::getName)
                                .toList()
                                : List.of()
                )

                .build();
    }

    public GameDetailsDto toGameDetailsDto(GameEntity game) {
        return GameDetailsDto.builder()
                .id(game.getId())
                .name(game.getName())
                .summary(game.getSummary())
                .storyline(game.getStoryline())
                .firstReleaseDate(game.getFirstReleaseDate())
                .totalRating(game.getTotalRating())
                .totalRatingCount(game.getTotalRatingCount())
                .status(game.getStatus())
                .aggregatedRating(game.getAggregatedRating())
                .aggregatedRatingCount(game.getAggregatedRatingCount())
                .rating(game.getRating())
                .hypes(game.getHypes())
                .cover(game.getCover())
                .platforms(game.getPlatforms())
                .genres(game.getGenres())
                .themes(game.getThemes())
                .screenshots(game.getScreenshots())
                .videos(game.getVideos())
                .artworks(game.getArtworks())
                .createdAt(game.getCreatedAt())
                .updatedAt(game.getUpdatedAt())
                .keywords(game.getKeywords())
                .isPopular(game.getIsPopular())
                .alternativeNames(game.getAlternativeNames())
                .franchise(game.getFranchise())
                .similarGames(game.getSimilarGames())
                .dlcs(game.getDlcs())
                .collections(game.getCollections())
                .gameModes(game.getGameModes())
                .externalGames(game.getExternalGames())
                .involvedCompanies(game.getInvolvedCompanies())
                .gameLocalizations(game.getGameLocalizations())
                .languageSupports(game.getLanguageSupports())
                .bundles(game.getBundles())
                .remakes(game.getRemakes())
                .build();
    }

    public GameEntity toEntity(Game game, boolean isPopular) {
        return GameEntity.builder()
                .id(String.valueOf(game.getId()))
                .name(game.getName())
                .summary(game.getSummary())
                .storyline(game.getStoryline())
                .firstReleaseDate(toDate(game.getFirstReleaseDate()))
                .totalRating(game.getTotalRating())
                .totalRatingCount(game.getTotalRatingCount())
                .status(game.getStatus())
                .aggregatedRating(game.getAggregatedRating())
                .aggregatedRatingCount(game.getAggregatedRatingCount())
                .rating(game.getRating())
                .hypes(game.getHypes())
                .cover(game.getCover())
                .platforms(game.getPlatforms())
                .genres(toGenreDbo(game.getGenres()))
                .themes(game.getThemes())
                .screenshots(game.getScreenshots())
                .videos(game.getVideos())
                .artworks(game.getArtworks())
                .createdAt(toDate(game.getCreatedAt()))
                .updatedAt(toDate(game.getUpdatedAt()))
                .keywords(game.getKeywords())
                .isPopular(isPopular)
                .alternativeNames(game.getAlternativeNames())
                .franchise(game.getFranchise())
                .similarGames(game.getSimilarGames())
                .dlcs(game.getDlcs())
                .collections(game.getCollections())
                .gameModes(game.getGameModes())
                .externalGames(game.getExternalGames())
                .involvedCompanies(game.getInvolvedCompanies())
                .gameLocalizations(game.getGameLocalizations())
                .languageSupports(game.getLanguageSupports())
                .bundles(game.getBundles())
                .remakes(game.getRemakes())
                .build();
    }


    public List<GenreEntity> toGenreDbo(List<Genre> genres) {
        if(Util.nullOrEmpty(genres)) {
            return null;
        }
        List<GenreEntity> genresDbo = new ArrayList<>(genres.size());
        for (Genre genre : genres) {
            GenreEntity genreEntity = GenreEntity.builder()
                    .slug(genre.getSlug())
                    .name(genre.getName())
                    .id(genre.getId())
                    .build();
            genresDbo.add(genreEntity);
        }
        return genresDbo;
    }

    public LocalDateTime toDate(Long timestamp) {
        if(Util.nullOrEmpty(timestamp)){
            return null;
        }
        return Instant.ofEpochSecond(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public List<PlatformEntity> toPlatformDbo(List<Platform> platforms) {

        List<PlatformEntity> platformsDbo = new ArrayList<>(platforms.size());

        for (Platform platform : platforms) {

            PlatformEntity platformEntity = PlatformEntity.builder()
                    .id(platform.getId())
                    .name(platform.getName())
                    .slug(platform.getSlug())
                    .abbreviation(platform.getAbbreviation())
                    .category(platform.getCategory())
                    .summary(platform.getSummary())
                    .build();

            platformsDbo.add(platformEntity);
        }

        return platformsDbo;
    }

    public List<GameTypeEntity> toGameTypeDbo(List<GameType> gameTypes) {

        List<GameTypeEntity> gameTypesDbo = new ArrayList<>(gameTypes.size());

        for (GameType gameType : gameTypes) {

            GameTypeEntity gameTypeEntity = GameTypeEntity.builder()
                    .id(gameType.getId())
                    .type(gameType.getType())
                    .build();

            gameTypesDbo.add(gameTypeEntity);
        }

        return gameTypesDbo;
    }

    public List<GameModeEntity> toGameModeDbo(List<GameMode> gameModes) {

        List<GameModeEntity> gameModesDbo = new ArrayList<>(gameModes.size());

        for (GameMode gameMode : gameModes) {

            GameModeEntity gameModeEntity = GameModeEntity.builder()
                    .id(gameMode.getId())
                    .name(gameMode.getName())
                    .slug(gameMode.getSlug())
                    .build();

            gameModesDbo.add(gameModeEntity);
        }

        return gameModesDbo;
    }



}