package com.mom.storefront_panoply.igdb.clients;

import com.mom.storefront_panoply.igdb.clients.config.FeignIgdbConfig;
import com.mom.storefront_panoply.igdb.model.Game;
import com.mom.storefront_panoply.igdb.model.PopularityPrimitive;
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
}
