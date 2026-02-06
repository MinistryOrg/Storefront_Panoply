package com.mom.storefront_panoply.games.service;

import com.mom.storefront_panoply.games.mapper.GameMapper;
import com.mom.storefront_panoply.games.model.dbo.*;
import com.mom.storefront_panoply.games.repository.GameRepository;
import com.mom.storefront_panoply.igdb.model.*;
import com.mom.storefront_panoply.igdb.service.IgdbService;
import com.mom.storefront_panoply.tools.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncGamesService {
    private final IgdbService igdbService;
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final MongoTemplate mongoTemplate;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Greece")
    public void syncGames() {
        // todo check the create at to not add all the games
        // todo add time to beat, is for each game or i can do it for all?
        log.info("Starting full game sync...");

        Set<Long> popularIds = igdbService.getPopularGamesIds();

        final int batchSize = 100;

        List<GameEntity> buffer = new ArrayList<>(batchSize);
        try {
            igdbService.getAllGames(games -> {

                for (Game game : games) {

                    boolean popular = popularIds.contains(game.getId());

                    GameEntity entity =
                            gameMapper.toEntity(game, popular);

                    buffer.add(entity);

                    // Save batch
                    if (buffer.size() >= batchSize) {

                        Util.bulkUpsert(buffer, GameEntity.class, mongoTemplate);
                        buffer.clear();
                    }
                }
            });

            // Save leftovers
            if (!buffer.isEmpty()) {
                Util.bulkUpsert(buffer, GameEntity.class, mongoTemplate);
                buffer.clear();
            }
        } catch (Exception e) {
            log.error("Failed to sync all games : " + e.getMessage());
            throw new RuntimeException(e);
        }

        log.info("Game sync completed successfully.");
    }


    // --- NEW: weekly job to sync filter info (genres/platforms/game types/game modes)
    // runs once a week at 07:00 on Mondays
    @Scheduled(cron = "0 0 7 * * MON", zone = "Europe/Greece")
    public void syncGameFilterInfo() {
        log.info("Starting weekly game filter info sync (Monday 07:00)...");

        try {
            // fetch lists from IgdbService
            List<Genre> genres = igdbService.getAllGenres();
            List<Platform> platforms = igdbService.getAllPlatforms();
            List<GameType> gameTypes = igdbService.getAllGameTypes();
            List<GameMode> gameModes = igdbService.getAllGameModes();

            Util.bulkUpsert(gameMapper.toGenreDbo(genres), GenreEntity.class, mongoTemplate);
            Util.bulkUpsert(gameMapper.toPlatformDbo(platforms), PlatformEntity.class, mongoTemplate);
            Util.bulkUpsert(gameMapper.toGameTypeDbo(gameTypes), GameTypeEntity.class, mongoTemplate);
            Util.bulkUpsert(gameMapper.toGameModeDbo(gameModes), GameModeEntity.class, mongoTemplate);

            // For now, log counts so you can plug in persistence later
            log.info("Fetched genres: {}", genres.size());
            log.info("Fetched platforms: {}", platforms.size());
            log.info("Fetched game types: {}", gameTypes.size());
            log.info("Fetched game modes: {}", gameModes.size());

        } catch (Exception e) {
            log.error("Failed to sync game filter info", e);
        }

        log.info("Weekly game filter info sync finished.");
    }

    // sync franchise

    // sync collection
}
