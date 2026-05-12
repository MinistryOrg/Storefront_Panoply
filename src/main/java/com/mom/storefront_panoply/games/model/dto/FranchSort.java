package com.mom.storefront_panoply.games.model.dto;

import org.springframework.data.domain.Sort;

public enum FranchSort {
    NAME_ASC,
    NAME_DESC;

    public Sort toSpringSort() {
        return switch (this) {
            case NAME_DESC -> Sort.by(Sort.Direction.DESC, "name");
            case NAME_ASC -> Sort.by(Sort.Direction.ASC, "name");
        };
    }
}
