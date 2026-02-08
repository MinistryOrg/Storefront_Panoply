package com.mom.storefront_panoply.sync;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/panoply")
@RequiredArgsConstructor
public class SyncController {
    private final SyncGamesService syncGamesService;
    @GetMapping("/soft-sync-game")
    public ResponseEntity<String> syncGames() {
        syncGamesService.syncGames();
        return ResponseEntity.ok("All good boss");
    }

    @GetMapping("/hard-sync-game")
    public ResponseEntity<String> hardSyncGames() {
        // todo is going to hard sync used only se case we want new information of new fields
        syncGamesService.syncGames();
        return ResponseEntity.ok("All good boss");
    }

    @GetMapping("/sync-game-filter")
    public ResponseEntity<String> syncGameFilterInfo() {
        syncGamesService.syncGameFilterInfo();
        return ResponseEntity.ok("All good boss");
    }

    @GetMapping("/sync-collections-franchises")
    public ResponseEntity<String> syncCollectionsFranchises() {
        syncGamesService.syncCollectionsFranchises();
        return ResponseEntity.ok("All good boss");
    }


}
