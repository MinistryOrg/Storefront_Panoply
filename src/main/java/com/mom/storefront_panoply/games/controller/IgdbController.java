package com.mom.storefront_panoply.games.controller;

import com.mom.storefront_panoply.games.model.Game;
import com.mom.storefront_panoply.games.service.IgdbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/panoply/games")
@RequiredArgsConstructor
public class IgdbController {
    private final IgdbService igdbService;

    @GetMapping("/popular")
    public ResponseEntity<List<Game>> getPopularGames() {
        return ResponseEntity.ok(igdbService.getPopularGames());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Game>> getGames() {
        return ResponseEntity.ok(igdbService.getAllGames());
    }
}
