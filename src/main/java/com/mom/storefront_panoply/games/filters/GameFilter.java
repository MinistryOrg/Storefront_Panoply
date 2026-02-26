package com.mom.storefront_panoply.games.filters;

import com.mom.storefront_panoply.igdb.model.Platform;
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
    private String gameName;
    private String companyName;
    private String platform;
    private Set<String> gameIds;
    private Boolean popular;
    private Boolean trending;
    private Boolean hiddenGems;
    private LocalDateTime createdAt; // recently added
    private LocalDateTime firstReleasedDate; // upcoming
    // todo sort based on hype, rating and name ??

}
