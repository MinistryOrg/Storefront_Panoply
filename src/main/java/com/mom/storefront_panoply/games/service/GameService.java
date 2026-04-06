package com.mom.storefront_panoply.games.service;

import com.mom.storefront_panoply.games.filters.CollectionFilter;
import com.mom.storefront_panoply.games.filters.FranchiseFilter;
import com.mom.storefront_panoply.games.filters.GameFilter;
import com.mom.storefront_panoply.games.filters.SearchFilter;
import com.mom.storefront_panoply.games.mapper.GameMapper;
import com.mom.storefront_panoply.games.model.dbo.*;
import com.mom.storefront_panoply.games.model.dto.*;
import com.mom.storefront_panoply.igdb.model.Franchise;
import com.mom.storefront_panoply.tools.PagedResponse;
import com.mom.storefront_panoply.tools.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameMapper gameMapper;
    private final MongoTemplate mongoTemplate;

    public PagedResponse<GameDto> getGames(GameFilter gamesFilter, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Get game with filter {}", gamesFilter);
        return PagedResponse.from(filterGames(gamesFilter, pageable, false), gameMapper::toGameDto);
    }

    public List<GameEntity> getGames(GameFilter gamesFilter, Boolean lightWeight) {
        return filterGames(gamesFilter, lightWeight);
    }

    public GameDetailsDto getGame(String gameId) {
        log.info("Get game with id {}", gameId);
        GameEntity gameEntity = getGameById(gameId);

        Set<Long> franchiseIds = Optional.ofNullable(
                gameEntity.getFranchises()
        ).orElse(Collections.emptyList())
                .stream().map(FranchiseRef::getId)
                .collect(Collectors.toSet());

        Set<Long> collectionIds = Optional.ofNullable(
                        gameEntity.getCollections()
                ).orElse(Collections.emptyList())
                .stream().map(CollectionRef::getId)
                .collect(Collectors.toSet());

        List<FranchiseDto> franchiseDto = getFranchises(FranchiseFilter.builder().ids(franchiseIds).build());
        List<CollectionDto> collectionsDto = getCollections(CollectionFilter.builder().ids(collectionIds).build());

        return gameMapper.toGameDetailsDto(gameEntity, franchiseDto, collectionsDto);
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
        Query query = buildGameQuery(filter, lightWeight, false);
        return mongoTemplate.find(query, GameEntity.class);
    }

    public Page<GameEntity> filterGames(
            GameFilter filter,
            Pageable pageable,
            Boolean startsWith
    ) {

        Query query;
        if (startsWith) {
            query = buildGameQuery(filter, true, true);
        } else {
            query = buildGameQuery(filter, true, false);
        }

        // Default sort
        if (Util.nullOrEmpty(filter.getSortBy())) {
            query.with(Sort.by(Sort.Direction.ASC, "_id"));
        } else {
            query.with(filter.getSortBy().toSpringSort());
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

    private Query buildCollectionFilter(CollectionFilter filter) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();
        // don't return all fields

        // Filter by single ID
        if (!Util.nullOrEmpty(filter.getId())) {
            criteriaList.add(Criteria.where("_id").is(filter.getId()));
        }

        // Filter by multiple IDs
        if (!Util.nullOrEmpty(filter.getIds())) {
            criteriaList.add(Criteria.where("_id").in(filter.getIds()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return query;
    }

    private Query buildFranchiseFilter(FranchiseFilter filter) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();
        // don't return all fields

        // Filter by single ID
        if (!Util.nullOrEmpty(filter.getId())) {
            criteriaList.add(Criteria.where("_id").is(filter.getId()));
        }

        // Filter by multiple IDs
        if (!Util.nullOrEmpty(filter.getIds())) {
            criteriaList.add(Criteria.where("_id").in(filter.getIds()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return query;
    }


    private Query buildGameQuery(GameFilter filter, Boolean lightWeight, Boolean startsWith) {
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
                    .include("platforms")
                    .include("screenshots.imageId")
                    .include("genres.name")
                    .include("type")
                    .include("firstReleaseDate")
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

        // filter by name that's start with
        if (!Util.nullOrEmpty(filter.getGameName()) && startsWith) {
            criteriaList.add(
                    Criteria.where("name")
                            .regex("^" + Pattern.quote(filter.getGameName())
            ));
        }

        // filter by company name that's start with
        if (!Util.nullOrEmpty(filter.getCompanyName()) && startsWith) {
            criteriaList.add(
                    Criteria.where("involvedCompanies.company.name")
                            .regex("^" + Pattern.quote(filter.getCompanyName())
            ));
        }

        // Trending
        if (Boolean.TRUE.equals(filter.getTrending())) {

            // Only released games
            Criteria releaseCriteria = Criteria.where("firstReleaseDate").lte(LocalDateTime.now());

            // Must have at least some ratings
            Criteria ratingCriteria = new Criteria().orOperator(
                    Criteria.where("aggregatedRatingCount").gt(0),
                    Criteria.where("totalRatingCount").gt(0)
            );

            // Trending high hype and recent release
            Criteria trendingSignal = new Criteria().andOperator(
                    Criteria.where("hypes").gte(40),
                    Criteria.where("firstReleaseDate").gte(LocalDateTime.now().minusMonths(6))
            );

            // Combine all
            Criteria trendingCriteria = new Criteria().andOperator(
                    releaseCriteria,
                    ratingCriteria,
                    trendingSignal
            );

            query.addCriteria(trendingCriteria);
        }

        // Popular
        if (Boolean.TRUE.equals(filter.getPopular())) {
            criteriaList.add(Criteria.where("isPopular").is(filter.getPopular()));
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
        if (!Util.nullOrEmpty(filter.getCreatedAt())) {
            criteriaList.add(Criteria.where("createdAt").gte(filter.getCreatedAt()));
        }

        // Released date with range
        if (!Util.nullOrEmpty(filter.getFirstReleasedDate()) && !Util.nullOrEmpty(filter.getLastReleasedDate())) {
            Criteria releaseDateCriteria = Criteria.where("firstReleaseDate");

            releaseDateCriteria = releaseDateCriteria.gte(filter.getFirstReleasedDate());

            releaseDateCriteria = releaseDateCriteria.lte(filter.getLastReleasedDate());

            criteriaList.add(releaseDateCriteria);
        }

        // Upcoming
        if (!Util.nullOrEmpty(filter.getFirstReleasedDate()) && Util.nullOrEmpty(filter.getLastReleasedDate())) {
            criteriaList.add(Criteria.where("firstReleaseDate").gte(filter.getFirstReleasedDate()));
        }

        // Platform
        if (!Util.nullOrEmpty(filter.getPlatforms())) {
            criteriaList.add(
                    Criteria.where("platforms")
                            .elemMatch(Criteria.where("name").in(filter.getPlatforms()))
            );
        }

        // franchise root games
        if (Boolean.TRUE.equals(filter.getFranchise())) {
            criteriaList.add(
                    new Criteria().andOperator(
                            Criteria.where("franchise").ne(null),
                            Criteria.where("parentGame").is(null),
                            Criteria.where("versionParent").is(null),
                            Criteria.where("versionTitle").is(null)
                    )
            );
        }

        // by game name starts with
        if (!Util.nullOrEmpty(filter.getGameName()) && !startsWith) {
            criteriaList.add(startsWithIgnoreCase("name", filter.getGameName()));
        }

        // by company name that starts with
        if (!Util.nullOrEmpty(filter.getCompanyName()) && !startsWith) {
            criteriaList.add(startsWithIgnoreCase("involvedCompanies.company.name", filter.getCompanyName()));
        }

        // Mode
        if (!Util.nullOrEmpty(filter.getMode())) {
            criteriaList.add(
                    Criteria.where("gameModes")
                            .elemMatch(Criteria.where("name").in(filter.getMode()))
            );
        }

        // Genres
        if (!Util.nullOrEmpty(filter.getGenres())) {
            criteriaList.add(
                    Criteria.where("genres")
                            .elemMatch(Criteria.where("name").in(filter.getGenres()))
            );
        }

        // Types - many values
        if (!Util.nullOrEmpty(filter.getTypes())) {
            criteriaList.add(
                    Criteria.where("type").in(filter.getTypes())
            );
        }

        // Rating
        if (!Util.nullOrEmpty(filter.getRating())) {
            criteriaList.add(Criteria.where("rating").gte(filter.getRating()));
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
        log.info("Get the game search filters...");
        List<GenreEntity> genreEntities = mongoTemplate.findAll(GenreEntity.class);
        List<GameModeEntity> modeEntities = mongoTemplate.findAll(GameModeEntity.class);
        List<PlatformEntity> platformEntities = mongoTemplate.findAll(PlatformEntity.class);
        List<GameTypeEntity> types = mongoTemplate.findAll(GameTypeEntity.class);
        return new GameSearchFilters(genreEntities, modeEntities, platformEntities, types);
    }

    public CollectionsResponse getCollection(Integer size, Integer page) {
        log.info("Get the collection...");
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

        // Build franchise DTOs
        List<CollectionDto> collectionDtos = new ArrayList<>(entities.size());
        for (CollectionEntity collectionEntity : entities) {
            collectionDtos.add(gameMapper.toCollection(collectionEntity));
        }

        // Wrap in Page
        Page<CollectionDto> pageResult = new PageImpl<>(collectionDtos, pageable, total);


        return CollectionsResponse.builder()
                .collections(PagedResponse.from(pageResult))
                .build();
    }

    public GameSearchResult searchGame(SearchFilter searchFilter, Integer page, Integer size) {
        log.info("Search game with filter: {}", searchFilter);
        Pageable pageable = PageRequest.of(page, size);


        PagedResponse<GameDto> byName = PagedResponse.from(filterGames(GameFilter.builder().
                gameName(searchFilter.getInput())
                        .mode(searchFilter.getMode())
                        .types(searchFilter.getTypes())
                        .firstReleasedDate(searchFilter.getFirstReleasedDate())
                        .lastReleasedDate(searchFilter.getLastReleasedDate())
                        .genres(searchFilter.getGenres())
                        .platforms(searchFilter.getPlatforms())
                        .rating(searchFilter.getRating())
                        .build(), pageable, true),
                gameMapper::toGameDto);

        PagedResponse<GameDto> byCompany = PagedResponse.from(filterGames(GameFilter.builder().companyName(searchFilter.getInput()).build(),
                pageable, true), gameMapper::toGameDto);

        return GameSearchResult.builder().gamesByCompany(byCompany).gamesByName(byName).build();
    }

    public List<FranchiseDto> getFranchises(FranchiseFilter filter) {
        log.info("Get the franchises...");

        Query query = buildFranchiseFilter(filter);

        List<FranchiseEntity> franchises = mongoTemplate.find(query, FranchiseEntity.class);
        List<FranchiseDto> franchiseDtos = new ArrayList<>(franchises.size());

        // Build franchise DTOs
        for (FranchiseEntity franchise : franchises) {
            franchiseDtos.add(gameMapper.toFranchise(franchise));
        }

        return franchiseDtos;
    }

    public List<CollectionDto> getCollections(CollectionFilter filter) {
        log.info("Get collections...");

        Query query = buildCollectionFilter(filter);

        List<CollectionEntity> collectionEntities = mongoTemplate.find(query, CollectionEntity.class);
        List<CollectionDto> collections = new ArrayList<>(collectionEntities.size());

        for (CollectionEntity collectionEntity : collectionEntities) {
            collections.add(gameMapper.toCollection(collectionEntity));
        }

        return collections;
    }



    public FranchisesResponse getFranchise(Integer size, Integer page, FranchiseFilter filter) {
        log.info("Get the franchise...");
        Pageable pageable = PageRequest.of(page, size);
        Query query = buildFranchiseFilter(filter);

        // Count total franchises
        long total = mongoTemplate.count(query, FranchiseEntity.class);

        // Fetch paginated franchises
        query.with(pageable);
        List<FranchiseEntity> franchises = mongoTemplate.find(query, FranchiseEntity.class);
        List<FranchiseDto> franchiseDtos = new ArrayList<>(franchises.size());

        // Build franchise DTOs
        for (FranchiseEntity franchise : franchises) {
            franchiseDtos.add(gameMapper.toFranchise(franchise));
        }

        // Wrap in Page
        Page<FranchiseDto> pageResult = new PageImpl<>(franchiseDtos, pageable, total);

        return FranchisesResponse.builder()
                .franchises(PagedResponse.from(pageResult))
                .build();
    }


    public Set<String> getGameIds(List<GameEntity> games) {
        if (Util.nullOrEmpty(games)) {
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
