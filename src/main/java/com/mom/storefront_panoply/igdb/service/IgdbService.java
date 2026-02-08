package com.mom.storefront_panoply.igdb.service;

import com.mom.storefront_panoply.igdb.clients.IgdbClient;
import com.mom.storefront_panoply.igdb.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class IgdbService {
    private final IgdbClient igdbClient;

    /*
        todo :
            -get collection
            -get franchise
            -fix the query to get only the latest updated or created to not update all the games every time
     */

    public Set<Long> getPopularGamesIds() {

        String popularityBody =
                "fields game_id,value,popularity_type; " +
                        "sort value desc; " +
                        "limit 20; " +
                        "where popularity_type = 1;";

        // Call IGDB popularity endpoint
        List<PopularityPrimitive> popular =
                igdbClient.getPopularGames(popularityBody);

        Set<Long> gameIds = new HashSet<>(popular.size());

        for (PopularityPrimitive popularityPrimitive : popular) {
            gameIds.add(popularityPrimitive.getGameId());
        }

        return gameIds;
    }

    public List<Genre> getAllGenres() {
        String body = "fields id,name;" +
                "limit 500;" +
                "offset 0;" +
                "sort id asc;";

        try {
           List<Genre> genres = igdbClient.getGenres(body);
           return genres;
        } catch (Exception e) {
            log.error("Failed to get the genres {}", e);
            throw e;
        }
    }

    public List<Platform> getAllPlatforms() {
        String body = "fields id,name;" +
                "limit 500;" +
                "offset 0;" +
                "sort id asc;";

        try {
            List<Platform> platforms = igdbClient.getPlatforms(body);
            return platforms;
        } catch (Exception e) {
            log.error("Failed to get the platforms {}", e);
            throw e;
        }
    }

    public List<GameType> getAllGameTypes() {
        String body = "fields type;" +
                "limit 500;" +
                "offset 0;" +
                "sort id asc;";

        try {
            List<GameType> gameTypes = igdbClient.getGameTypes(body);
            return gameTypes;
        } catch (Exception e) {
            log.error("Failed to get the game types {}", e);
            throw e;
        }
    }

    public List<GameMode> getAllGameModes() {
        String body = "fields id,name;" +
                "limit 500;" +
                "offset 0;" +
                "sort id asc;";

        try {
            List<GameMode> gameMode = igdbClient.getGameMode(body);
            return gameMode;
        } catch (Exception e) {
            log.error("Failed to get the game types {}", e);
            throw e;
        }
    }

    public void getAllGames(Consumer<List<Game>> pageConsumer) {
        int offset = 0;
        final int limit = 100;

        log.info("Starting IGDB full sync...");

        while (true) {

            // recompute size on every iteration so offset is correct
            String size = "limit " + limit + "; offset " + offset + ";";

            String body = """
                    fields
                   
                      name,
                      summary,
                      storyline,
                      first_release_date,
                      created_at, updated_at,
                      status, hypes,

                      total_rating,
                      total_rating_count,
                      aggregated_rating,
                      aggregated_rating_count,
                      rating,
                  
                      cover.image_id,
                      screenshots.image_id,
                      artworks.image_id,
                      artworks.artwork_type,
                      videos.video_id,
                      videos.name,
                   
                      platforms.*,
                      game_status.status,
                    
                    
                      genres.name,
                      themes.name,
                      game_modes.name,
                      player_perspectives.name,
                      keywords.name,
                    
                      age_ratings.organization.name,
                      age_ratings.rating_category.rating,
                      age_ratings.rating_category.organization.name,
                      age_ratings.rating_cover_url,
                      age_ratings.synopsis,
                      age_ratings.rating_content_descriptions.description,
                    
                      involved_companies.developer,
                      involved_companies.publisher,
                      involved_companies.company.name,
                      involved_companies.supporting,
                    
                      game_engines.name,
                      game_localizations.region.name,
                      language_supports.language.name,
                      language_supports.language_support_type.name,
                    
                      version_title,
                      version_parent.name,
                      version_parent.cover.image_id,
                      ports.*,
                      remakes.name,
                      remakes.cover.image_id,
                      remasters.name,
                      remasters.cover.image_id,
                    
                      game_type,
                      parent_game.name,
                      parent_game.cover.url,
                    
                      dlcs.name,
                      dlcs.cover.image_id,
                      dlcs.game_type,
                    
                      expansions.name,
                      expansions.cover.image_id,
                      expansions.game_type,
                    
                      standalone_expansions.name,
                      standalone_expansions.cover.url,
                    
                      bundles.name,
                      bundles.cover.image_id,
                    
                      external_games.game,
                      external_games.name,
                      external_games.url,
                      external_games.platform.name,
                      external_games.external_game_source.name,
                    
                      franchise,
                      franchises.name,
                      franchises.url,
                      franchises.games.name,
                      franchises.games.cover.image_id,
                      franchises.games.game_type,
                      franchises.games.version_parent,
                      franchises.games.version_parent.name,
                      franchises.games.first_release_date,
                      franchises.games.rating,
                      franchises.games.hypes,
                    
                      collections.name,
                      collections.games.name,
                      collections.games.cover.image_id,
                      collections.games.game_type,
                      collections.games.id,
                      collections.games.first_release_date,
                      collections.as_parent_relations.*,
                      collection.as_child_relations.*,
                    
                      websites.*,
                      websites.type.*,
                    
                      similar_games.name,
                      similar_games.cover.image_id,
                      similar_games.rating,
                    
                      alternative_names.name;
                    
                    """
                    + size + ";";


            List<Game> games;

            try {
                games = igdbClient.getGames(body);
            } catch (Exception e) {
                log.error("Failed at offset {}", offset, e);
                break;
            }

            if (games == null || games.isEmpty()) {
                log.info("No more games.");
                break;
            }

            int fetched = games.size();

            log.info("Fetched {} games at offset {}", games.size(), offset);

            pageConsumer.accept(games);

            games.clear();

            if (fetched < limit) {
                log.info("Last page reached.");
                break;
            }

            offset += limit;

            try {
                Thread.sleep(260);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.info("IGDB streaming finished.");
    }

    public void getAllCollections(Consumer<List<Collection>> pageConsumer) {
        int offset = 0;
        final int limit = 100;

        log.info("Starting IGDB collections full sync...");

        while (true) {
            String size = "limit " + limit + "; offset " + offset + ";";
            String body = """
                    fields as_child_relations,as_parent_relations,checksum,created_at,games,name,slug,type,updated_at,url;
                """ + size + ";";

            List<Collection> collections;

            try {
                collections = igdbClient.getCollections(body);
            } catch (Exception e) {
                log.error("Failed fetching collections at offset {}", offset, e);
                break;
            }

            if (collections == null || collections.isEmpty()) {
                log.info("No more collections.");
                break;
            }

            int fetched = collections.size();
            log.info("Fetched {} collections at offset {}", fetched, offset);

            pageConsumer.accept(collections);
            collections.clear();

            if (fetched < limit) {
                log.info("Last page of collections reached.");
                break;
            }

            offset += limit;

            try {
                Thread.sleep(260); // respect IGDB rate limits
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.info("IGDB collections streaming finished.");
    }

    public void getAllFranchises(Consumer<List<Franchise>> pageConsumer) {
        int offset = 0;
        final int limit = 100;

        log.info("Starting IGDB franchises full sync...");

        while (true) {
            String size = "limit " + limit + "; offset " + offset + ";";

            String body = """
                    fields checksum,created_at,games,name,slug,updated_at,url;
                """ + size + ";";

            List<Franchise> franchises;

            try {
                franchises = igdbClient.getFranchises(body);
            } catch (Exception e) {
                log.error("Failed fetching franchises at offset {}", offset, e);
                break;
            }

            if (franchises == null || franchises.isEmpty()) {
                log.info("No more franchises.");
                break;
            }

            int fetched = franchises.size();
            log.info("Fetched {} franchises at offset {}", fetched, offset);

            pageConsumer.accept(franchises);
            franchises.clear();

            if (fetched < limit) {
                log.info("Last page of franchises reached.");
                break;
            }

            offset += limit;

            try {
                Thread.sleep(260);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.info("IGDB franchises streaming finished.");
    }

}

