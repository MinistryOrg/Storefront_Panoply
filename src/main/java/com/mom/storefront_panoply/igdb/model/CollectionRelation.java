package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CollectionRelation {

    private Long id;

    @JsonProperty("parent_collection")
    private Collection parentCollection;

    @JsonProperty("child_collection")
    private Collection childCollection;
}

