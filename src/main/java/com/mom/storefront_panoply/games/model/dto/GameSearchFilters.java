package com.mom.storefront_panoply.games.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mom.storefront_panoply.games.model.dbo.GameModeEntity;
import com.mom.storefront_panoply.games.model.dbo.GameTypeEntity;
import com.mom.storefront_panoply.games.model.dbo.GenreEntity;
import com.mom.storefront_panoply.games.model.dbo.PlatformEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameSearchFilters {
    private List<GenreEntity> genres;
    private List<GameModeEntity> modes;
    private List<PlatformEntity> platforms;
    private List<GameTypeEntity> types;
}
