package com.mom.storefront_panoply.igdb.clients;

import com.mom.storefront_panoply.igdb.clients.config.FeignIgdbConfig;
import com.mom.storefront_panoply.igdb.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "igdbClient", url = "https://api.igdb.com/v4", configuration = FeignIgdbConfig.class)
public interface IgdbClient {

    @PostMapping(value = "/popularity_primitives", consumes = "text/plain", produces = "application/json")
    List<PopularityPrimitive> getPopularGames(@RequestBody String body);

    @PostMapping(value = "/games", consumes = "text/plain", produces = "application/json")
    List<Game> getGames(@RequestBody String body);

    @PostMapping(value = "/genres", consumes = "text/plain", produces = "application/json")
    List<Genre> getGenres(@RequestBody String body);

    @PostMapping(value = "/platforms", consumes = "text/plain", produces = "application/json")
    List<Platform> getPlatforms(@RequestBody String body);

    @PostMapping(value = "/game_types", consumes = "text/plain", produces = "application/json")
    List<GameType> getGameTypes(@RequestBody String body);

    @PostMapping(value = "/game_modes", consumes = "text/plain", produces = "application/json")
    List<GameMode> getGameMode(@RequestBody String body);

    @PostMapping(value = "/game_time_to_beats", consumes = "text/plain", produces = "application/json")
    List<GameTimeToBeats> getGameTimeToBeats(@RequestBody String body);

    @PostMapping(value = "/franchises", consumes = "text/plain", produces = "application/json")
    List<Franchise> getFranchises(@RequestBody String body);

    @PostMapping(value = "/collections", consumes = "text/plain", produces = "application/json")
    List<Collection> getCollections(@RequestBody String body);
}
