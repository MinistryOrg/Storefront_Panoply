package com.mom.storefront_panoply.games.filters;

import com.mom.storefront_panoply.games.model.dto.FranchSort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseFilter {
    private Long id;
    private Set<Long> ids;
    private String name;
    private FranchSort sortBy;
}
