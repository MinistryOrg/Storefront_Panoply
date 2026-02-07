package com.mom.storefront_panoply.games.service;

import com.mom.storefront_panoply.games.filters.GameFilter;
import com.mom.storefront_panoply.games.mapper.GameMapper;
import com.mom.storefront_panoply.games.model.dbo.*;
import com.mom.storefront_panoply.games.model.dto.*;
import com.mom.storefront_panoply.games.repository.GameRepository;
import com.mom.storefront_panoply.igdb.model.Game;
import com.mom.storefront_panoply.igdb.model.Genre;
import com.mom.storefront_panoply.igdb.service.IgdbService;
import com.mom.storefront_panoply.tools.PagedResponse;
import com.mom.storefront_panoply.tools.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final IgdbService igdbService;
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final MongoTemplate mongoTemplate;

    public PagedResponse<GameDto> getGames(GameFilter gamesFilter, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        return PagedResponse.from(filterGames(gamesFilter, pageable), gameMapper::toGameDto);
    }

    public GameDetailsDto getGame(String gameId) {
        return gameMapper.toGameDetailsDto(getGameById(gameId));
    }

    public GameEntity getGameById(String gameId) {
        if (Util.nullOrEmpty(gameId)) {
            return null;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(gameId));

        return mongoTemplate.findOne(query, GameEntity.class);
    }


    public Page<GameEntity> filterGames(
            GameFilter filter,
            Pageable pageable
    ) {

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        // Filter by ID
        if (!Util.nullOrEmpty(filter.getGameId())) {
            criteriaList.add(Criteria.where("id").is(filter.getGameId()));
        }

        // Popular
        if (!Util.nullOrEmpty(filter.getPopular())) {
            criteriaList.add(Criteria.where("isPopular").is(filter.getPopular()));
        }

        // Trending
        if (Boolean.TRUE.equals(filter.getTrending())) {
            criteriaList.add(Criteria.where("hypes").gt(100));
        }

        // Hidden Gems (high rating low votes?)
        if (Boolean.TRUE.equals(filter.getHiddenGems())) {
            criteriaList.add(
                    new Criteria().andOperator(
                            Criteria.where("rating").gte(8),
                            Criteria.where("totalRatingCount").lt(500)
                    )
            );
        }

        // Recently added
        if (!Util.nullOrEmpty(filter.getCreatedAt())) {
            criteriaList.add(
                    Criteria.where("createdAt").gte(filter.getCreatedAt())
            );
        }

        // Upcoming
        if (!Util.nullOrEmpty(filter.getFirstReleasedDate())) {
            criteriaList.add(
                    Criteria.where("firstReleaseDate").gte(filter.getFirstReleasedDate())
            );
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(
                    criteriaList.toArray(new Criteria[0])
            ));
        }

        // Pagination
        query.with(pageable);

        // Execute
        List<GameEntity> results =
                mongoTemplate.find(query, GameEntity.class);

        long total =
                mongoTemplate.count(Query.of(query).limit(-1).skip(-1),
                        GameEntity.class);

        return new PageImpl<>(results, pageable, total);
    }

    public GameSearchFilters getGameSearchFilters() {
        List<GenreEntity> genreEntities = mongoTemplate.findAll(GenreEntity.class);
        List<GameModeEntity> modeEntities = mongoTemplate.findAll(GameModeEntity.class);
        List<PlatformEntity> platformEntities = mongoTemplate.findAll(PlatformEntity.class);
        List<GameTypeEntity> types = mongoTemplate.findAll(GameTypeEntity.class);
        return new GameSearchFilters(genreEntities, modeEntities, platformEntities, types);
    }

    public CollectionsResponse getCollection(Integer size, Integer page) {

        Pageable pageable = PageRequest.of(page, size);

        Query query = new Query();

        // Count before pagination
        long total = mongoTemplate.count(query, CollectionEntity.class);

        // Apply pagination
        query.with(pageable);

        // Fetch data
        List<CollectionEntity> entities =
                mongoTemplate.find(query, CollectionEntity.class);

        // Convert List to  Page
        Page<CollectionEntity> pageResult =
                new PageImpl<>(entities, pageable, total);

        return CollectionsResponse.builder()
                .collections(PagedResponse.from(pageResult))
                .build();
    }

    public FranchisesResponse getFranchise(Integer size, Integer page) {

        Pageable pageable = PageRequest.of(page, size);

        Query query = new Query();

        // Count before pagination
        long total = mongoTemplate.count(query, CollectionEntity.class);

        // Apply pagination
        query.with(pageable);

        // Fetch data
        List<FranchiseEntity> entities =
                mongoTemplate.find(query, FranchiseEntity.class);

        // Convert List to  Page
        Page<FranchiseEntity> pageResult =
                new PageImpl<>(entities, pageable, total);

        return FranchisesResponse.builder()
                .franchises(PagedResponse.from(pageResult))
                .build();
    }



}
