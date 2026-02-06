package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Website {
    private Long id;
    private String url;
    private WebsiteType type;

    public Website(Long id) {
        this.id = id;
    }
}

