package com.mom.storefront_panoply.games.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class GameFilter {
    private String gameId;
    private Set<String> gameIds;
    private Boolean popular;
    private Boolean trending;
    private Boolean hiddenGems;
    private LocalDateTime createdAt; // recently added
    private LocalDateTime firstReleasedDate; // upcoming
    // todo sort based on hype, rating and name ??

}
