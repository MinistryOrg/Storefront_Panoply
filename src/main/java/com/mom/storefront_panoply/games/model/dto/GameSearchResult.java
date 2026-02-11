package com.mom.storefront_panoply.games.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GameSearchResult {
    private List<GameDto> gamesByName;
    private List<GameDto> gamesByCompany;
}
