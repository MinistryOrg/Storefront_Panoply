package com.mom.storefront_panoply.games.service;

import com.mom.storefront_panoply.games.filters.GameFilter;
import com.mom.storefront_panoply.games.mapper.GameMapper;
import com.mom.storefront_panoply.games.model.dbo.GameEntity;
import com.mom.storefront_panoply.games.model.dto.GameDto;
import com.mom.storefront_panoply.games.repository.GameRepository;
import com.mom.storefront_panoply.igdb.model.Game;
import com.mom.storefront_panoply.igdb.service.IgdbService;
import com.mom.storefront_panoply.tools.PagedResponse;
import com.mom.storefront_panoply.tools.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    public PagedResponse<GameDto> findGames(GameFilter gamesFilter, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GameEntity> entities;

        if(Util.nullOrEmpty(gamesFilter)) {
           entities = gameRepository.findAll(pageable);
           return PagedResponse.from(entities, gameMapper::toDto);
        }

        return PagedResponse.from(filterGames(gamesFilter, pageable),gameMapper::toDto);
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

    public void syncGames() {
        // todo check the create at to not add all the games
        log.info("Starting full game sync...");

        Set<Long> popularIds = igdbService.getPopularGamesIds();

        final int batchSize = 100;

        List<GameEntity> buffer = new ArrayList<>(batchSize);

        igdbService.streamAllGames(games -> {

            for (Game game : games) {

                boolean popular = popularIds.contains(game.getId());

                GameEntity entity =
                        gameMapper.toEntity(game, popular);

                buffer.add(entity);

                // Save batch
                if (buffer.size() >= batchSize) {

                    gameRepository.saveAll(buffer);

                    buffer.clear();
                }
            }
        });

        // Save leftovers
        if (!buffer.isEmpty()) {
            gameRepository.saveAll(buffer);
            buffer.clear();
        }

        log.info("Game sync completed successfully.");
    }
}
