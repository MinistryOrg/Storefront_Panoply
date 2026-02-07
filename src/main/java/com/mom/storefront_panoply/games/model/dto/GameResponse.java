package com.mom.storefront_panoply.games.model.dto;

import com.mom.storefront_panoply.tools.PagedResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {
    private PagedResponse<GameDto> games;
}
