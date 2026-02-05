package com.mom.storefront_panoply.igdb.service;

import com.mom.storefront_panoply.igdb.clients.IgdbClient;
import com.mom.storefront_panoply.igdb.model.Game;
import com.mom.storefront_panoply.igdb.model.PopularityPrimitive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IgdbService {
    private final IgdbClient igdbClient;
    /*
            TODO :
                    -hype in the game it return in the popular
                    -getGames minimal info
                    -getGame all the info
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

    public List<Game> getAllGameTest() {
        int offset = 0;
        final int limit = 500;

        log.info("Starting full IGDB sync…");

        String size = "limit " + limit + "; offset " + offset + ";";

        String body = "fields " +
                "name, summary, storyline, first_release_date, created_at, updated_at," +
                "total_rating, total_rating_count, status, cover.image_id, aggregated_rating, aggregated_rating_count, rating, similar_games, platforms.*," +
                "genres.name," +
                "themes.name," +
                "game_modes.name," +
                "player_perspectives.name," +
                "age_ratings.*, age_ratings.organization," +
                "involved_companies.developer, involved_companies.publisher, involved_companies.company.name, involved_companies.supporting," +
                "game_engines.name," +
                "game_localizations.region.name," +
                "language_supports.language.name, language_supports.language_support_type.name," +
                "ports.name, ports.cover.url," +
                "remakes.name, remakes.cover.url," +
                "remasters.name, remasters.cover.url," +
                "version_title, parent_game.name, parent_game.cover.url," +
                "dlcs.name, dlcs.cover.url," +
                "expansions.name, expansions.cover.url," +
                "standalone_expansions.name, standalone_expansions.cover.url," +
                "bundles.name, bundles.cover.url," +
                "screenshots.image_id," +
                "videos.video_id," +
                "artworks.image_id," +
                "alternative_names.name," +
                "external_games.category, external_games.url," +
                "websites.*, websites.type.*," +
                "similar_games.name, similar_games.cover.url," +
                "keywords.name;" +
                size;

        log.info("Requesting IGDB page: offset={}, limit={}", offset, limit);

        List<Game> games;
        try {
            games = igdbClient.getGames(body);
        } catch (Exception e) {
            log.error("Failed to fetch games from IGDB at offset {}: {}", offset, e.getMessage(), e);
            throw e;
        }
        List<Game> allGames = new ArrayList<>(games);
        log.info("Total games fetched: {}", allGames.size());
        return allGames;
    }

    public void streamAllGames(Consumer<List<Game>> pageConsumer) {
        int offset = 0;
        final int limit = 200;
        String size = "limit " + limit + "; offset " + offset + ";";

        log.info("Starting IGDB full sync...");

        while (true) {

            String body = "fields" +
                    "  name, summary, storyline, first_release_date," +
                    "  total_rating, total_rating_count, status, cover.image_id, aggregated_rating, aggregated_rating_count, rating, similar_games, platforms.*," +
                    "  genres.name," +
                    "  themes.name," +
                    "  game_modes.name," +
                    "  player_perspectives.name," +
                    "  age_ratings.*, age_ratings.organization," +
                    "  involved_companies.developer, involved_companies.publisher, involved_companies.company.name, involved_companies.supporting," +
                    "  game_engines.name," +
                    "  game_localizations.region.name," +
                    "  language_supports.language.name, language_supports.language_support_type.name," +
                    "  ports.name, ports.cover.url," +
                    "  remakes.name, remakes.cover.url," +
                    "  remasters.name, remasters.cover.url," +
                    "  version_title, parent_game.name, parent_game.cover.url," +
                    "  dlcs.name, dlcs.cover.url," +
                    "  expansions.name, expansions.cover.url," +
                    "  standalone_expansions.name, standalone_expansions.cover.url," +
                    "  bundles.name, bundles.cover.url," +
                    "  screenshots.image_id," +
                    "  videos.video_id," +
                    "  artworks.image_id," +
                    "  alternative_names.name," +
                    "  external_games.category, external_games.url," +
                    "  websites.*, websites.type.*," +
                    "  similar_games.name, similar_games.cover.url," +
                    "  keywords.name;" + size;

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
}

