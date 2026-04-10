package com.mom.storefront_panoply.games.service;

import com.mom.storefront_panoply.games.model.dbo.GameEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameMigrationService {
    private final MongoTemplate mongoTemplate;

    public String migrateFranchiseToRef() {
        MongoCollection<Document> collection = mongoTemplate.getCollection("games");

        long scanned = 0;
        long updated = 0;

        try (var cursor = collection.find().batchSize(100).iterator()) {

            while (cursor.hasNext()) {
                Document game = cursor.next();
                scanned++;

                boolean changed = false;

                Object franchiseObj = game.get("franchise");
                if (franchiseObj instanceof Document franchiseDoc) {
                    game.put("franchise", toFranchiseRefDoc(franchiseDoc));
                    changed = true;
                }

                Object franchisesObj = game.get("franchises");
                if (franchisesObj instanceof List<?> franchiseList) {

                    List<Document> converted = new ArrayList<>();
                    boolean listChanged = false;

                    for (Object item : franchiseList) {
                        if (item instanceof Document doc) {
                            converted.add(toFranchiseRefDoc(doc));
                            listChanged = true;
                        } else {
                            converted.add((Document) item);
                        }
                    }

                    if (listChanged) {
                        game.put("franchises", converted);
                        changed = true;
                    }
                }

                if (changed) {
                    collection.replaceOne(
                            Filters.eq("_id", game.get("_id")),
                            game
                    );
                    updated++;
                }
            }
        }

        return "Scanned : " + scanned + " and updated : " + updated;
    }

    public String migrateCollectionsToRef() {
        MongoCollection<Document> collection = mongoTemplate.getCollection("games");

        long scanned = 0;
        long updated = 0;

        try (var cursor = collection.find()
                .projection(new Document("collections", 1))
                .batchSize(100)
                .iterator()) {

            while (cursor.hasNext()) {
                Document game = cursor.next();
                scanned++;

                Object collectionsObj = game.get("collections");
                if (!(collectionsObj instanceof List<?> collectionList) || collectionList.isEmpty()) {
                    continue;
                }

                List<Document> converted = new ArrayList<>();
                boolean changed = false;

                for (Object item : collectionList) {
                    Document doc = toDocument(item);
                    if (doc == null) {
                        converted.add(null);
                        continue;
                    }

                    converted.add(toCollectionRefDoc(doc));
                    changed = true;
                }

                if (changed) {
                    collection.updateOne(
                            Filters.eq("_id", game.get("_id")),
                            Updates.set("collections", converted)
                    );
                    updated++;
                }
            }
        }

        return scanned + " " + updated;
    }

    public String migrateSimilarGamesToRef() {
        MongoCollection<Document> collection = mongoTemplate.getCollection("games");

        long scanned = 0;
        long updated = 0;

        try (var cursor = collection.find().batchSize(100).iterator()) {

            while (cursor.hasNext()) {
                Document game = cursor.next();
                scanned++;

                Object similarGamesObj = game.get("similarGames");

                if (!(similarGamesObj instanceof List<?> similarGamesList) || similarGamesList.isEmpty()) {
                    continue;
                }

                List<Document> similarGameRefs = new ArrayList<>();
                boolean changed = false;

                for (Object item : similarGamesList) {

                    if (item instanceof String similarGameId) {

                        Document ref = new Document();
                        ref.put("_id", similarGameId);

                        similarGameRefs.add(ref);
                        changed = true;

                    } else if (item instanceof Document similarGameDoc) {

                        // already migrated
                        if (similarGameDoc.containsKey("_id") && !similarGameDoc.containsKey("slug")) {
                            similarGameRefs.add(similarGameDoc);
                            continue;
                        }

                        // old embedded SimilarGame object format
                        Object similarGameId = similarGameDoc.get("_id");
                        Object similarGameName = similarGameDoc.get("name");

                        if (similarGameId != null) {
                            Document ref = new Document();
                            ref.put("_id", String.valueOf(similarGameId));

                            if (similarGameName != null) {
                                ref.put("name", String.valueOf(similarGameName));
                            }

                            similarGameRefs.add(ref);
                            changed = true;
                        }
                    }
                }

                if (changed) {
                    game.put("similarGames", similarGameRefs);

                    collection.replaceOne(
                            Filters.eq("_id", game.get("_id")),
                            game
                    );

                    updated++;
                }
            }
        }

        return "Scanned : " + scanned + " and updated : " + updated;
    }
    private Document toCollectionRefDoc(Document collectionDoc) {
        Document ref = new Document();

        if (collectionDoc.containsKey("id")) {
            ref.put("id", collectionDoc.get("id"));
        }
        if (collectionDoc.containsKey("_id")) {
            ref.put("_id", collectionDoc.get("_id"));
        }

        if (collectionDoc.containsKey("name")) {
            ref.put("name", collectionDoc.get("name"));
        }
        if (collectionDoc.containsKey("url")) {
            ref.put("url", collectionDoc.get("url"));
        }
        if (collectionDoc.containsKey("artwork")) {
            ref.put("artwork", collectionDoc.get("artwork"));
        }

        return ref;
    }

    private Document toDocument(Object item) {
        if (item instanceof Document doc) {
            return doc;
        }
        if (item instanceof Map<?, ?> map) {
            return new Document((Map<String, Object>) map);
        }
        return null;
    }

    private Document toFranchiseRefDoc(Document franchiseDoc) {
        Document ref = new Document();

        if (franchiseDoc.containsKey("id")) {
            ref.put("id", franchiseDoc.get("id"));
        }

        if (franchiseDoc.containsKey("_id")) {
            ref.put("_id", franchiseDoc.get("_id"));
        }

        if (franchiseDoc.containsKey("name")) {
            ref.put("name", franchiseDoc.get("name"));
        }
        if (franchiseDoc.containsKey("url")) {
            ref.put("url", franchiseDoc.get("url"));
        }
        if (franchiseDoc.containsKey("artwork")) {
            ref.put("artwork", franchiseDoc.get("artwork"));
        }

        return ref;
    }
}
