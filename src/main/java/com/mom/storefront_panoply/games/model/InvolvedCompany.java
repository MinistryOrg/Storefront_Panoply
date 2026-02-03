package com.mom.storefront_panoply.games.model;

import lombok.Data;

@Data
public class InvolvedCompany {

    private Boolean developer;
    private Boolean publisher;
    private Boolean supporting;

    private Company company;
}

