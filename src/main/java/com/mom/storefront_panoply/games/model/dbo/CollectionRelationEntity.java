package com.mom.storefront_panoply.games.model.dbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mom.storefront_panoply.igdb.model.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionRelationEntity{

    private Long id;

    @JsonProperty("parent_collection")
    private CollectionEntity parentCollection;

    @JsonProperty("child_collection")
    private CollectionEntity childCollection;

    public CollectionRelationEntity(Long id) {
        this.id = id;
    }
}
