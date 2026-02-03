package com.mom.storefront_panoply.games.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Company {
    private Long id;
    private CompanyInfo company;
    private String name;
    private Boolean developer;
    private Boolean publisher;
}