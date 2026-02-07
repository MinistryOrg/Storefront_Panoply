package com.mom.storefront_panoply.games.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mom.storefront_panoply.games.model.dbo.CollectionEntity;
import com.mom.storefront_panoply.igdb.model.Collection;
import com.mom.storefront_panoply.tools.PagedResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionsResponse {
    PagedResponse<CollectionEntity> collections;
}
