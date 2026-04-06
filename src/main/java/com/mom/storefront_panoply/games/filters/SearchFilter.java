package com.mom.storefront_panoply.games.filters;

import com.mom.storefront_panoply.games.model.dto.GameSort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilter {
    private String input;
    private Set<String> mode;
    private Set<String> genres;
    private Set<Integer> types;
    private Set<String> platforms;
    private Double rating;
    private LocalDateTime firstReleasedDate;
    private LocalDateTime lastReleasedDate;
    private GameSort sortBy;
}
