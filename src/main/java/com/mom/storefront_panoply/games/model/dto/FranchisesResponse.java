package com.mom.storefront_panoply.games.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mom.storefront_panoply.games.model.dbo.FranchiseEntity;
import com.mom.storefront_panoply.igdb.model.Franchise;
import com.mom.storefront_panoply.tools.PagedResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FranchisesResponse {
    private PagedResponse<FranchiseDto> franchises;
}
