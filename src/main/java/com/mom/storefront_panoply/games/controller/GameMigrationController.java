package com.mom.storefront_panoply.games.controller;

import com.mom.storefront_panoply.games.service.GameMigrationService;
import com.mom.storefront_panoply.syncGames.model.MetadataType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("panoply/migrations")
@RequiredArgsConstructor
public class GameMigrationController {

    private final GameMigrationService gameMigrationService;
    private final MongoTemplate mongoTemplate;
    @DeleteMapping("delete-metadata")
    public ResponseEntity<String> howTheHellMetadataExist(){
        Query query = new Query();
        query.addCriteria(Criteria.where("type").exists(true));

        mongoTemplate.remove(query, "franchises");
        return ResponseEntity.ok("Deleted franchises metadata");
    }
    @PostMapping("franchise-ref")
    public ResponseEntity<String> migrateFranchiseToRef() {
        String result = "Franchise : " + gameMigrationService.migrateFranchiseToRef();
        result += " Collection : " + gameMigrationService.migrateCollectionsToRef();
        return ResponseEntity.ok(result);
    }

    @PostMapping("similar-games-ref")
    public ResponseEntity<String> migrateSimilarGamesToRef() {
        String result = gameMigrationService.migrateSimilarGamesToRefV2();
        return ResponseEntity.ok(result);
    }
}