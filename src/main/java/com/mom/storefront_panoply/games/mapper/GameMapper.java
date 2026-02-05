package com.mom.storefront_panoply.games.mapper;

import com.mom.storefront_panoply.games.model.dbo.GameEntity;
import com.mom.storefront_panoply.games.model.dto.GameDto;
import com.mom.storefront_panoply.igdb.model.Game;
import com.mom.storefront_panoply.igdb.model.Genre;
import com.mom.storefront_panoply.igdb.model.Screenshot;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameMapper {

    public GameDto toDto(GameEntity entity) {

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
                                .map(Genre::getName)
                                .toList()
                                : List.of()
                )

                .build();
    }


    public GameEntity toEntity(Game game, boolean isPopular) {

        return GameEntity.builder()
                .id(String.valueOf(game.getId()))
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

                .isPopular(isPopular)
                .build();
    }
}