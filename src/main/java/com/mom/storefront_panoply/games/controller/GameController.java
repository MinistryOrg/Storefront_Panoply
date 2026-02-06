package com.mom.storefront_panoply.games.controller;

import com.mom.storefront_panoply.games.filters.GameFilter;
import com.mom.storefront_panoply.games.model.dbo.GameEntity;
import com.mom.storefront_panoply.games.model.dto.*;
import com.mom.storefront_panoply.games.service.GameService;
import com.mom.storefront_panoply.tools.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/panoply")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping("games")
    public ResponseEntity<GameResponse> getGames(@RequestBody(required = false) GameFilter gamesFilter, @RequestParam(defaultValue = "10") Integer size,
                                                           @RequestParam(defaultValue = "0") Integer page) {
        PagedResponse<GameDto> games = gameService.getGames(gamesFilter, size, page);
        return ResponseEntity.ok(GameResponse.builder()
                        .message("Success")
                        .status("200")
                        .games(games)
                .build());
    }

    @GetMapping("game")
    public ResponseEntity<GameDetailsResponse> getGame(@RequestParam String gameId) {
        GameDetailsDto game = gameService.getGame(gameId);
        return ResponseEntity.ok(GameDetailsResponse.builder()
                .message("Success")
                .status("200")
                .gamesDetails(game)
                .build());
    }

    @GetMapping("game-search-filters")
    public ResponseEntity<GameSearchFilters> gameSearchFilters() {
        return ResponseEntity.ok(gameService.getGameSearchFilters());
    }
}
