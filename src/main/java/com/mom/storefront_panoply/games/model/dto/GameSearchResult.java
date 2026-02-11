package com.mom.storefront_panoply.games.model.dto;

import com.mom.storefront_panoply.tools.PagedResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GameSearchResult {
    private PagedResponse<GameDto> gamesByName;
    private PagedResponse<GameDto> gamesByCompany;
}
