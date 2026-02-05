package com.mom.storefront_panoply.games.service;

import com.mom.storefront_panoply.games.filters.GameFilter;
import com.mom.storefront_panoply.games.mapper.GameMapper;
import com.mom.storefront_panoply.games.model.dbo.GameEntity;
import com.mom.storefront_panoply.games.model.dto.GameDto;
import com.mom.storefront_panoply.games.model.dto.GameResponse;
import com.mom.storefront_panoply.games.repository.GameRepository;
import com.mom.storefront_panoply.igdb.model.Game;
import com.mom.storefront_panoply.igdb.service.IgdbService;
import com.mom.storefront_panoply.pagination.model.PagedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public PagedResponse<GameDto> findGames(GameFilter gamesFilter, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GameEntity> entities =
                gameRepository.findAll(pageable);
        return PagedResponse.from(entities, gameMapper::toDto);
    }

//    public void syncGames() {
//
//        log.info("Starting game sync...");
//
//        // 1. Get popular IDs
//        Set<Long> popularIds = igdbService.getPopularGamesIds();
//
//        log.info("Popular games ids: {}", popularIds);
//
//        // 2. Get all games
//        List<Game> igdbGames = igdbService.getAllGames();
//
//        if (igdbGames.isEmpty()) {
//            log.warn("No games fetched from IGDB");
//            return;
//        }
//
//        List<GameEntity> entities = new ArrayList<>();
//
//        for (Game game : igdbGames) {
//
//            boolean isPopular = popularIds.contains(game.getId());
//
//            GameEntity entity =
//                    gameMapper.toEntity(game, isPopular);
//
//            entities.add(entity);
//        }
//
//        gameRepository.saveAll(entities);
//
//        log.info("Saved {} games", entities.size());
//    }

    public void syncGames() {

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
