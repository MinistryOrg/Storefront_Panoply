package com.mom.storefront_panoply.games.mapper;

import com.mom.storefront_panoply.games.model.dbo.*;
import com.mom.storefront_panoply.games.model.dto.CollectionDto;
import com.mom.storefront_panoply.games.model.dto.FranchiseDto;
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
        List<String> screenshots = null;
        if (entity.getScreenshots() != null && !entity.getScreenshots().isEmpty()) {
            screenshots = new ArrayList<>(entity.getScreenshots().size());
            for (Screenshot s : entity.getScreenshots()) {
                screenshots.add(s.getImageId());
            }
        }

        List<String> genres = null;
        if (entity.getGenres() != null && !entity.getGenres().isEmpty()) {
            genres = new ArrayList<>(entity.getGenres().size());
            for (GenreEntity g : entity.getGenres()) {
                genres.add(g.getName());
            }
        }

        return GameDto.builder()
                .id(Long.valueOf(entity.getId()))
                .name(entity.getName())
                .summary(entity.getSummary())
                .coverImageId(entity.getCover() != null ? entity.getCover().getImageId() : null)
                .totalRating(entity.getTotalRating() != null ? entity.getTotalRating().toString() : null)
                .rating(entity.getRating() != null ? entity.getRating().toString() : null)
                .screenshotsImageId(screenshots)
                .genreName(genres != null ? genres : new ArrayList<>())
                .video(entity.getVideos())
                .build();
    }

    public List<GameDto> toGameDto(List<GameEntity> entities) {
        List<GameDto> gameDtos = new ArrayList<>(entities.size());
        for (GameEntity entity : entities) {
            List<String> screenshots = null;
            if (entity.getScreenshots() != null && !entity.getScreenshots().isEmpty()) {
                screenshots = new ArrayList<>(entity.getScreenshots().size());
                for (Screenshot s : entity.getScreenshots()) {
                    screenshots.add(s.getImageId());
                }
            }

            List<String> genres = null;
            if (entity.getGenres() != null && !entity.getGenres().isEmpty()) {
                genres = new ArrayList<>(entity.getGenres().size());
                for (GenreEntity g : entity.getGenres()) {
                    genres.add(g.getName());
                }
            }

            gameDtos.add(GameDto.builder()
                    .id(Long.valueOf(entity.getId()))
                    .name(entity.getName())
                    .summary(entity.getSummary())
                    .coverImageId(entity.getCover() != null ? entity.getCover().getImageId() : null)
                    .totalRating(entity.getTotalRating() != null ? entity.getTotalRating().toString() : null)
                    .rating(entity.getRating() != null ? entity.getRating().toString() : null)
                    .screenshotsImageId(screenshots)
                    .genreName(genres != null ? genres : new ArrayList<>())
                    .video(entity.getVideos())
                    .build());
        }
        return gameDtos;
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

    public CollectionDto toCollection(CollectionEntity collection, List<GameEntity> gameEntities) {
        return CollectionDto.builder()
                .id(collection.getId())
                .name(collection.getName())
                .games(gameEntities)
                .build();
    }



    public CollectionEntity toCollection(Collection collection) {
        return CollectionEntity.builder()
                .id(collection.getId())
                .name(collection.getName())
                .games(toGames(collection.getGames()))
                .asParentRelations(toCollectionRelationDbo(collection.getAsParentRelations()))
                .asChildRelations(toCollectionRelationDbo(collection.getAsChildRelations()))
                .build();
    }


    public List<CollectionRelationEntity> toCollectionRelationDbo(List<CollectionRelation> collectionRelations) {
        if(Util.nullOrEmpty(collectionRelations)) {
            return null;
        }
        List<CollectionRelationEntity> collectionRelationsDbo = new ArrayList<>(collectionRelations.size());
        for (CollectionRelation collectionRelation : collectionRelations) {
            collectionRelationsDbo.add(CollectionRelationEntity
                    .builder()
                            .id(collectionRelation.getId())
                            .childCollection(toCollectionEntity(collectionRelation.getChildCollection()))
                            .parentCollection(toCollectionEntity(collectionRelation.getChildCollection()))
                    .build());
        }
        return collectionRelationsDbo;
    }

    public List<CollectionEntity> toCollection(List<Collection> collection) {
        List<CollectionEntity> collectionDbo = new ArrayList<>(collection.size());
        for (Collection c : collection) {
            collectionDbo.add(CollectionEntity.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .games(toGames(c.getGames()))
                    .asParentRelations(toCollectionRelationDbo(c.getAsParentRelations()))
                    .asChildRelations(toCollectionRelationDbo(c.getAsChildRelations()))
                    .build());
        }
        return collectionDbo;
    }

    public CollectionEntity toCollectionEntity(Collection collection) {
        if(Util.nullOrEmpty(collection)) {
            return null;
        }
        return CollectionEntity.builder()
                .id(collection.getId())
                .name(collection.getName())
                .games(toGames(collection.getGames()))
                .asParentRelations(toCollectionRelationDbo(collection.getAsParentRelations()))
                .asChildRelations(toCollectionRelationDbo(collection.getAsChildRelations()))
                .build();
    }


    public List<GameEntity> toGames(List<Game> games) {
        if(Util.nullOrEmpty(games)) {
            return null;
        }
        List<GameEntity> gameDbo = new ArrayList<>(games.size());
        for (Game game : games) {
            gameDbo.add(GameEntity.builder()
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
                    .build());
        }
        return gameDbo;
    }


    public FranchiseEntity toFranchise(Franchise franchise) {
        return FranchiseEntity.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .url(franchise.getUrl())
                .games(toGames(franchise.getGames()))
                .build();
    }

    public FranchiseDto toFranchise(FranchiseEntity franchise, List<GameEntity> game) {
        return FranchiseDto.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .url(franchise.getUrl())
                .games(toGameDto(game))
                .build();
    }



}