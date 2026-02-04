package com.mom.storefront_panoply.games.controller;

import com.mom.storefront_panoply.games.filters.GameFilter;
import com.mom.storefront_panoply.games.model.dto.GameDto;
import com.mom.storefront_panoply.games.model.dto.GameResponse;
import com.mom.storefront_panoply.games.service.GameService;
import com.mom.storefront_panoply.pagination.model.PagedResponse;
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
        PagedResponse<GameDto> games = gameService.findGames(gamesFilter, size, page);
        return ResponseEntity.ok(GameResponse.builder()
                        .message("Success")
                        .status("200")
                        .games(games)
                .build());
    }

    @GetMapping("/sync")
    public ResponseEntity<String> syncGames() {
        gameService.syncGames();
        return ResponseEntity.ok("All good boss");
    }




}
