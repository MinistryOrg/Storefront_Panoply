package com.mom.storefront_panoply.games.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class GameFilter {
    private String gameId;
    private Boolean popular;
    private Boolean trending;
    private Boolean hiddenGems;
    private LocalDateTime createdAt; // recently added
    private LocalDateTime firstReleasedDate; // upcoming
    // todo sort based on hype, rating and name ??

}
