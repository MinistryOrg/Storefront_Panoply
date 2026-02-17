package com.mom.storefront_panoply.games.service;

import com.mom.storefront_panoply.games.filters.GameFilter;
import com.mom.storefront_panoply.games.filters.SearchFilter;
import com.mom.storefront_panoply.games.mapper.GameMapper;
import com.mom.storefront_panoply.games.model.dbo.*;
import com.mom.storefront_panoply.games.model.dto.*;
import com.mom.storefront_panoply.igdb.model.Game;
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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameMapper gameMapper;
    private final MongoTemplate mongoTemplate;

    public PagedResponse<GameDto> getGames(GameFilter gamesFilter, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        return PagedResponse.from(filterGames(gamesFilter, pageable, false), gameMapper::toGameDto);
    }

    public List<GameEntity> getGames(GameFilter gamesFilter, Boolean lightWeight) {
        return filterGames(gamesFilter, lightWeight);
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

    public List<GameEntity> filterGames(GameFilter filter, Boolean lightWeight) {
        Query query = buildGameQuery(filter, lightWeight);
        return mongoTemplate.find(query, GameEntity.class);
    }

    public Page<GameEntity> filterGames(
            GameFilter filter,
            Pageable pageable,
            Boolean startsWith
    ) {

        Query query;
        if (startsWith) {
            query = buildSearchGameQuery(filter);
        } else {
            query = buildGameQuery(filter, true);
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

    private Query buildGameQuery(GameFilter filter, Boolean lightWeight) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();
        // don't return all fields
        if (lightWeight) {
            query.fields()
                    .include("_id")
                    .include("name")
                    .include("summary")
                    .include("cover.imageId")
                    .include("totalRating")
                    .include("rating")
                    .include("screenshots.imageId")
                    .include("genres.name")
                    .include("videos");
        }

        // Filter by single ID
        if (!Util.nullOrEmpty(filter.getGameId())) {
            criteriaList.add(Criteria.where("_id").is(filter.getGameId()));
        }

        // Filter by multiple IDs
        if (!Util.nullOrEmpty(filter.getGameIds())) {
            criteriaList.add(Criteria.where("_id").in(filter.getGameIds()));
        }

        // Filter by name
        if (!Util.nullOrEmpty(filter.getGameName())) {
            criteriaList.add(Criteria.where("name").is(filter.getGameId()));
        }

        // filter by name
        if (!Util.nullOrEmpty(filter.getGameName())) {
            criteriaList.add(
                    Criteria.where("name")
                            .regex(filter.getGameName(), "i")
            );
        }

        // filter by company name
        if (!Util.nullOrEmpty(filter.getCompanyName())) {
            criteriaList.add(
                    Criteria.where("involvedCompanies.company.name")
                            .regex(filter.getCompanyName(), "i")
            );
        }


        // Popular
        if (filter.getPopular() != null) {
            criteriaList.add(Criteria.where("isPopular").is(filter.getPopular()));
        }

        // Trending
        if (Boolean.TRUE.equals(filter.getTrending())) {
            criteriaList.add(Criteria.where("hypes").gt(40));
        }

        // Hidden Gems
        if (Boolean.TRUE.equals(filter.getHiddenGems())) {
            criteriaList.add(
                    new Criteria().andOperator(
                            Criteria.where("rating").gte(8),
                            Criteria.where("totalRatingCount").lt(500)
                    )
            );
        }

        // Recently added
        if (filter.getCreatedAt() != null) {
            criteriaList.add(Criteria.where("createdAt").gte(filter.getCreatedAt()));
        }

        // Upcoming
        if (filter.getFirstReleasedDate() != null) {
            criteriaList.add(Criteria.where("firstReleaseDate").gte(filter.getFirstReleasedDate()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return query;
    }

    private Query buildSearchGameQuery(GameFilter filter) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        // by game name starts with
        if (!Util.nullOrEmpty(filter.getGameName())) {
            criteriaList.add(startsWithIgnoreCase("name", filter.getGameName()));
        }

        // by company name that starts with
        if (!Util.nullOrEmpty(filter.getCompanyName())) {
            criteriaList.add(startsWithIgnoreCase("involvedCompanies.company.name", filter.getCompanyName()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return query;
    }


    private Criteria startsWithIgnoreCase(String field, String value) {
        return Criteria.where(field)
                .regex("^" + Pattern.quote(value), "i");
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

        // Collect all game IDs
        Set<String> allGameIds = new HashSet<>();
        for (CollectionEntity collectionEntity : entities) {
            List<GameEntity> games = collectionEntity.getGames();
            if (!Util.nullOrEmpty(games)) {
                for (GameEntity game : games) {
                    if (game.getId() != null) {
                        allGameIds.add(game.getId());
                    }
                }
            }
        }

        // Fetch all games in one query
        List<GameEntity> allGames = getGames(GameFilter.builder().gameIds(allGameIds).build(), true);

        // Map game ID to game entity using a HashMap
        Map<String, GameEntity> gameMap = new HashMap<>();
        for (GameEntity game : allGames) {
            if (game.getId() != null) {
                gameMap.put(game.getId(), game);
            }
        }

        // Build franchise DTOs
        List<CollectionDto> collectionDtos = new ArrayList<>(entities.size());
        for (CollectionEntity collectionEntity : entities) {
            List<GameEntity> collectionGames = new ArrayList<>();
            List<GameEntity> games = collectionEntity.getGames();
            if (!Util.nullOrEmpty(games)) {
                for (GameEntity g : games) {
                    GameEntity mapped = gameMap.get(g.getId());
                    if (mapped != null) {
                        collectionGames.add(mapped);
                    }
                }
            }
            collectionDtos.add(gameMapper.toCollection(collectionEntity, collectionGames));
        }

        // Wrap in Page
        Page<CollectionDto> pageResult = new PageImpl<>(collectionDtos, pageable, total);


        return CollectionsResponse.builder()
                .collections(PagedResponse.from(pageResult))
                .build();
    }

    public GameSearchResult searchGame(SearchFilter searchFilter, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponse<GameDto> byName = PagedResponse.from(filterGames(GameFilter.builder().
                gameName(searchFilter.getInput()).build(), pageable, true), gameMapper::toGameDto);

        PagedResponse<GameDto> byCompany= PagedResponse.from(filterGames(GameFilter.builder().
                gameName(searchFilter.getInput()).build(), pageable, true), gameMapper::toGameDto);

        return GameSearchResult.builder().gamesByCompany(byCompany).gamesByName(byName).build();
    }

    public FranchisesResponse getFranchise(Integer size, Integer page) {

        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query();

        // Count total franchises
        long total = mongoTemplate.count(query, FranchiseEntity.class);

        // Fetch paginated franchises
        query.with(pageable);
        List<FranchiseEntity> franchises = mongoTemplate.find(query, FranchiseEntity.class);

        // Collect all game IDs
        Set<String> allGameIds = new HashSet<>();
        for (FranchiseEntity franchise : franchises) {
            List<GameEntity> games = franchise.getGames();
            if (!Util.nullOrEmpty(games)) {
                for (GameEntity game : games) {
                    if (game.getId() != null) {
                        allGameIds.add(game.getId());
                    }
                }
            }
        }

        // Fetch all games in one query
        List<GameEntity> allGames = getGames(GameFilter.builder().gameIds(allGameIds).build(), true);

        // Map game ID to game entity using a HashMap
        Map<String, GameEntity> gameMap = new HashMap<>();
        for (GameEntity game : allGames) {
            if (game.getId() != null) {
                gameMap.put(game.getId(), game);
            }
        }

        // Build franchise DTOs
        List<FranchiseDto> franchiseDtos = new ArrayList<>(franchises.size());
        for (FranchiseEntity franchise : franchises) {
            List<GameEntity> franchiseGames = new ArrayList<>();
            List<GameEntity> games = franchise.getGames();
            if (!Util.nullOrEmpty(games)) {
                for (GameEntity g : games) {
                    GameEntity mapped = gameMap.get(g.getId());
                    if (mapped != null) {
                        franchiseGames.add(mapped);
                    }
                }
            }
            franchiseDtos.add(gameMapper.toFranchise(franchise, franchiseGames));
        }

        // Wrap in Page
        Page<FranchiseDto> pageResult = new PageImpl<>(franchiseDtos, pageable, total);

        return FranchisesResponse.builder()
                .franchises(PagedResponse.from(pageResult))
                .build();
    }


    public Set<String> getGameIds(List<GameEntity> games) {
        if(Util.nullOrEmpty(games)) {
            return new HashSet<>();
        }

        Set<String> set = new HashSet<>(games.size());

        for (GameEntity game : games) {
            String id = game.getId();
            if (id != null) {
                set.add(id);
            }
        }

        return set;
    }


}
