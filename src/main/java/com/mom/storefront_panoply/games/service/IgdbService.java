package com.mom.storefront_panoply.games.service;

import com.mom.storefront_panoply.clients.IgdbClient;
import com.mom.storefront_panoply.games.model.Game;
import com.mom.storefront_panoply.games.model.PopularityPrimitive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public List<Game> getPopularGames() {

        String popularityBody =
                "fields game_id,value,popularity_type; " +
                        "sort value desc; " +
                        "limit 20; " +
                        "where popularity_type = 2;";

        // Call IGDB popularity endpoint
        List<PopularityPrimitive> popular =
                igdbClient.getPopularGames(popularityBody);

        List<Long> gameIds = new ArrayList<>(popular.size());

        for (PopularityPrimitive popularityPrimitive : popular) {
            gameIds.add(popularityPrimitive.getGameId());
        }

        if (gameIds.isEmpty()) {
            return List.of();
        }

        String gameBody =
                "fields *; " +
                        "where id = (" + gameIds.stream().map(String::valueOf).collect(Collectors.joining(",")) + ");";

        return igdbClient.getGames(gameBody);
    }

    public List<Game> getAllGames() {
        int offset = 0;
        int totalSaved = 0;
        String size = "limit 500; offset " + offset + ";";
        String body =                 "fields" +
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

        log.info("Starting full IGDB sync…");
        List<Game> allGames = new ArrayList<>();
        while (true) {
            List<Game> games = igdbClient.getGames(body);
            if (games.isEmpty()) {
                // todo check if the games exist in the database

                // todo save the games to database if doesn't exist in the database

                break;
            }

            offset += 500;
            try {
                Thread.sleep(250); // rate limit safe
            } catch (InterruptedException ignored) {}
        }

        // todo get the popular games
        return allGames;
    }


}
