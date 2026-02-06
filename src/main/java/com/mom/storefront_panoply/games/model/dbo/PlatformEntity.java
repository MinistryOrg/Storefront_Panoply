package com.mom.storefront_panoply.games.model.dbo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mom.storefront_panoply.igdb.model.PlatformCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("platform")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlatformEntity {
    @Id
    private Long id;
    private String name;
    private String slug;
    private String abbreviation;
    private PlatformCategory category;
    private String summary;
}