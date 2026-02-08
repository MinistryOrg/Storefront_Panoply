package com.mom.storefront_panoply.games.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mom.storefront_panoply.games.model.dbo.GameEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FranchiseDto {
    @Id
    private Long id;
    private String name;
    private String url;

    private List<GameDto> games;
}
