package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LanguageSupport {
    private Long id;
    private Language language;
    private LanguageSupportType language_support_type;

    public LanguageSupport(Long id) {
        this.id = id;
    }
}
