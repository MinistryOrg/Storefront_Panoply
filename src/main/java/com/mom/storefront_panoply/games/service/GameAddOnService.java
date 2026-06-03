package com.mom.storefront_panoply.games.service;

import com.mom.storefront_panoply.games.mapper.GameMapper;
import com.mom.storefront_panoply.games.model.dbo.GameEntity;
import com.mom.storefront_panoply.games.model.dto.GameAddon;
import com.mom.storefront_panoply.igdb.model.Game;
import com.mom.storefront_panoply.tools.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.UncheckedTimeoutException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameAddOnService {
    private final GameMapper gameMapper;
    private final MongoTemplate mongoTemplate;

    public List<GameAddon> getGameAddons(String gameId, String type) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if(Util.nullOrEmpty(gameId)) {
            throw new RuntimeException("The game id cannot be null or empty in the get game addons");
        }

        Criteria.where("parentGame").is(null);

        criteriaList.add(
                new Criteria().orOperator(
                        Criteria.where("versionParent._id").is(gameId),
                        Criteria.where("parentGame._id").is(gameId)
                )
        );

        if(!Util.nullOrEmpty(type)) {
            criteriaList.add(Criteria.where("type").is(type));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        List<GameEntity> gameEntities = mongoTemplate.find(query, GameEntity.class);

        if(Util.nullOrEmpty(gameEntities)) {
            return new ArrayList<>();
        }

        return gameMapper.toGameAddons(gameEntities);
    }
}
