package com.mom.storefront_panoply.igdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameTimeToBeats {
    private Long id;
    @JsonProperty("game_id")
    private Long gameId;
    private Long hastily;
    private Long normally;
    private Long completely;
    private Long count;
}
