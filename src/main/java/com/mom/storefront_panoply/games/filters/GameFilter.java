package com.mom.storefront_panoply.games.filters;

import com.mom.storefront_panoply.games.model.dbo.GameTypeEntity;
import com.mom.storefront_panoply.games.model.dto.GameSort;
import com.mom.storefront_panoply.igdb.model.GameType;
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
    private Set<String> gameIds;
    private String gameName;
    private Set<Integer> types;
    private Set<String> mode;
    private Set<String> genres;
    private String companyName;
    private Set<String> platforms;
    private Boolean franchise;
    private Boolean popular;
    private Boolean trending;
    private Boolean hiddenGems;
    private LocalDateTime createdAt; // recently added
    private LocalDateTime firstReleasedDate; // upcoming
    private LocalDateTime lastReleasedDate;
    private GameSort sortBy;
    private Double rating;
}
