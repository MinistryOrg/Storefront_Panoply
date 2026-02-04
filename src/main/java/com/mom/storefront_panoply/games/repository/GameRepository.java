package com.mom.storefront_panoply.games.repository;

import com.mom.storefront_panoply.games.model.dbo.GameEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository
        extends MongoRepository<GameEntity, String> {
}
