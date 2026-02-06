package com.mom.storefront_panoply.games.model.dbo;

import com.mom.storefront_panoply.igdb.model.CollectionRelation;
import com.mom.storefront_panoply.igdb.model.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "collections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionEntity {
    @Id
    private Long id;
    private String name;
    private List<Game> games;
    private List<CollectionRelation> asParentRelations;
    private List<CollectionRelation> asChildRelations;
}
