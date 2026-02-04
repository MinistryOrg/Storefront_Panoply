package com.mom.storefront_panoply.igdb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Website {
    private Long id;
    private String url;
    private WebsiteType type;
}

