package com.mom.storefront_panoply.igdb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStatus {
    private int id;
    private String status;

    public GameStatus(Integer id) {
        this.id = id;
    }
}
