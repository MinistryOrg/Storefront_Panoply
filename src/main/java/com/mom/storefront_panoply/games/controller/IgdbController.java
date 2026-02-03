package com.mom.storefront_panoply.games.controller;

import com.mom.storefront_panoply.games.model.Game;
import com.mom.storefront_panoply.games.service.IgdbService;
import lombok.RequiredArgsConstructor;
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
    public List<Game> getPopularGames() {
        return igdbService.getPopularGames();
    }

    @GetMapping("/all")
    public List<Game> getGames() {
        return igdbService.getAllGames();
    }
}
