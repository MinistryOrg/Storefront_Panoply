package com.mom.storefront_panoply.games.controller;

import com.mom.storefront_panoply.games.service.GameMigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("panoply/migrations")
@RequiredArgsConstructor
public class GameMigrationController {

    private final GameMigrationService gameMigrationService;

    @PostMapping("franchise-ref")
    public ResponseEntity<String> migrateFranchiseToRef() {
        String result = "Franchise : " + gameMigrationService.migrateFranchiseToRef();
        result += " Collection : " + gameMigrationService.migrateCollectionsToRef();
        return ResponseEntity.ok(result);
    }
}