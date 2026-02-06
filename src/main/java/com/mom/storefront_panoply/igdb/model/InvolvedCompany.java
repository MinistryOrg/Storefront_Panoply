package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvolvedCompany {
    private Long id;
    private Boolean developer;
    private Boolean publisher;
    private Boolean supporting;

    private Company company;

    public InvolvedCompany(Long id) {
        this.id = id;
    }
}

