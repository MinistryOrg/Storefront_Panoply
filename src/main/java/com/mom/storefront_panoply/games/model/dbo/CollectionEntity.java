package com.mom.storefront_panoply.games.model.dbo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mom.storefront_panoply.games.model.dto.GameDto;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionEntity {
    @Id
    private Long id;
    private String name;
    private List<GameEntity> games;
    private List<CollectionRelationEntity> asParentRelations;
    private List<CollectionRelationEntity> asChildRelations;
}
