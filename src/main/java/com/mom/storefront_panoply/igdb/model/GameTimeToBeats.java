package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameTimeToBeats {
    private Long id;
    @JsonProperty("game_id")
    private Long gameId;
    private Long hastily;
    private Long normally;
    private Long completely;
    private Long count;
}
