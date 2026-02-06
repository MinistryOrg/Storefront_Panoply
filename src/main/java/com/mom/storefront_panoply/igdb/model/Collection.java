package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Collection {

    private Long id;
    private String name;

    private List<Game> games;

    @JsonProperty("as_parent_relations")
    private List<CollectionRelation> asParentRelations;

    @JsonProperty("as_child_relations")
    private List<CollectionRelation> asChildRelations;

    public Collection(Long id) {
        this.id = id;
    }
}
