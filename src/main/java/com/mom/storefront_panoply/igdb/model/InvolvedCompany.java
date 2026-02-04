package com.mom.storefront_panoply.igdb.model;

import lombok.Data;

@Data
public class InvolvedCompany {

    private Boolean developer;
    private Boolean publisher;
    private Boolean supporting;

    private Company company;
}

