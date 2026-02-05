package com.mom.storefront_panoply.games.repository;

import com.mom.storefront_panoply.games.model.dbo.GameEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameRepository
        extends MongoRepository<GameEntity, String> {

    List<GameEntity> getGameEntityByIsPopular(Boolean isPopular);

    List<GameEntity> getGameEntityById(String gameId);
}
