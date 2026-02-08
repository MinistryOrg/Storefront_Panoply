package com.mom.storefront_panoply.sync;

import com.mom.storefront_panoply.games.filters.GameFilter;
import com.mom.storefront_panoply.games.mapper.GameMapper;
import com.mom.storefront_panoply.games.model.dbo.*;
import com.mom.storefront_panoply.games.service.GameService;
import com.mom.storefront_panoply.igdb.model.*;
import com.mom.storefront_panoply.igdb.service.IgdbService;
import com.mom.storefront_panoply.sync.model.SyncMetadata;
import com.mom.storefront_panoply.sync.model.MetadataType;
import com.mom.storefront_panoply.sync.model.SyncType;
import com.mom.storefront_panoply.tools.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncGamesService {
    private final IgdbService igdbService;
    private final GameMapper gameMapper;
    private final GameService gameService;
    private final MongoTemplate mongoTemplate;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Athens")
    public void syncGames() {
        // todo check the create at to not add all the games
        // todo add time to beat, is for each game or i can do it for all?
        log.info("Starting full game sync...");

        SyncMetadata syncMetadata = getSyncMetadata(MetadataType.GAME);

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
        saveSyncMetadata(MetadataType.GAME);
        log.info("Game sync completed successfully.");
    }


    @Scheduled(cron = "0 0 8 * * *", zone = "Europe/Athens")
    public void syncCollectionsFranchises() {
        log.info("Starting full game, collection, and franchise sync...");

        final int batchSize = 100;
        // Sync Collections
        List<CollectionEntity> collectionBuffer = new ArrayList<>(batchSize);
        try {
            igdbService.getAllCollections(collections -> {
                for (Collection collection : collections) {
                    //Set<String> gameIds = Util.extractValues(collection.getGames());
                    //List<GameEntity> gameEntities = gameService.getGames(GameFilter.builder().gameIds(gameIds).build(), true);
                    CollectionEntity entity = gameMapper.toCollection(collection); // map IGDB Collection -> Mongo entity
                    collectionBuffer.add(entity);
                    if (collectionBuffer.size() >= batchSize) {
                        Util.bulkUpsert(collectionBuffer, CollectionEntity.class, mongoTemplate);
                        collectionBuffer.clear();
                    }
                }
            });

            if (!collectionBuffer.isEmpty()) {
                Util.bulkUpsert(collectionBuffer, CollectionEntity.class, mongoTemplate);
                collectionBuffer.clear();
            }
            log.info("Collection sync completed successfully.");
        } catch (Exception e) {
            log.error("Failed to sync collections: {}", e.getMessage(), e);
        }

        // sync franchises
        List<FranchiseEntity> franchiseBuffer = new ArrayList<>(batchSize);
        try {
            igdbService.getAllFranchises(franchises -> {
                for (Franchise franchise : franchises) {
                    FranchiseEntity entity = gameMapper.toFranchise(franchise);
                    franchiseBuffer.add(entity);

                    if (franchiseBuffer.size() >= batchSize) {
                        Util.bulkUpsert(franchiseBuffer, FranchiseEntity.class, mongoTemplate);
                        franchiseBuffer.clear();
                    }
                }
            });

            if (!franchiseBuffer.isEmpty()) {
                Util.bulkUpsert(franchiseBuffer, FranchiseEntity.class, mongoTemplate);
                franchiseBuffer.clear();
            }
            log.info("Franchise sync completed successfully.");
        } catch (Exception e) {
            log.error("Failed to sync franchises: {}", e.getMessage(), e);
        }

        log.info("Full sync completed successfully.");
    }

    // runs once a week at 07:00 on Mondays
    @Scheduled(cron = "0 0 7 * * MON", zone = "Europe/Athens")
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

    public void saveSyncMetadata(MetadataType type) {
        Instant now = Instant.now();
        long epochSeconds = now.getEpochSecond();

        SyncMetadata updatedSyncMetadata = SyncMetadata.builder()
                .type(type)
                .timestamp(epochSeconds)
                .build();

        // check if already exist
        SyncMetadata metadata = getSyncMetadata(type);
        if (!Util.nullOrEmpty(metadata)) {
            updatedSyncMetadata.setId(metadata.getId());
        }

        mongoTemplate.insert(updatedSyncMetadata);
    }

    public SyncMetadata getSyncMetadata(MetadataType type) {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(type));
        SyncMetadata metadata = mongoTemplate.findOne(query, SyncMetadata.class);

        if (Util.nullOrEmpty(metadata)) {
            return mongoTemplate.insert(SyncMetadata.builder().type(MetadataType.GAME).timestamp(null).id(UUID.randomUUID().toString()).build());
        }

        return metadata;
    }

    // todo times to beat

    public String getQuery(SyncType type) {
        if (type.equals(SyncType.HARD)) {
          return "";
        }
        return "";
    }
}
