package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Collection {

    private Long id;
    private String name;

    private List<Game> games;

    @JsonProperty("as_parent_relations")
    private List<CollectionRelation> asParentRelations;

    @JsonProperty("as_child_relations")
    private List<CollectionRelation> asChildRelations;
}
