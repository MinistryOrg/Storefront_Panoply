package com.mom.storefront_panoply.tools;


import org.bson.Document;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Util {
    public static boolean nullOrEmpty(Object obj) {
        boolean nullOrEmpty = obj == null ||
                (obj instanceof CharSequence && ((CharSequence) obj).isEmpty());

        if(obj instanceof Collection && ((Collection<?>) obj).isEmpty()) nullOrEmpty = true;
        if(obj instanceof Map && ((Map<?, ?>) obj).isEmpty()) nullOrEmpty = true;

        return nullOrEmpty;
    }

    public static <T> void bulkUpsert(List<T> entities, Class<T> entityClass, MongoTemplate mongoTemplate) {

        if (entities == null || entities.isEmpty()) {
            return;
        }

        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, entityClass);

        for (T entity : entities) {

            Document doc = new Document();
            mongoTemplate.getConverter().write(entity, doc);

            Object id = doc.get("_id");
            if (id == null) {
                throw new IllegalStateException("Entity " + entityClass.getSimpleName() + " has no @Id field set");
            }

            // Query by _id
            Query query = Query.query(Criteria.where("_id").is(id));

            // Remove _id from the fields we want to update (can't modify it)
            doc.remove("_id");

            Update update = new Update();

            // Set fields for update
            for (String key : doc.keySet()) {
                update.set(key, doc.get(key));
            }

            // Ensure the _id is inserted if document is new
            update.setOnInsert("_id", id);

            bulkOps.upsert(query, update);
        }

        bulkOps.execute();
    }
}
