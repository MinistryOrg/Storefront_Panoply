package com.mom.storefront_panoply.games.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageSupport {
    private Long id;
    private Language language;
    private LanguageSupportType language_support_type;
}
