package com.levelUp.multiplayerSnake.dao;

import com.levelUp.multiplayerSnake.models.FPlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerDao {

    UUID insertPlayer(UUID id, FPlayer player);

    default UUID insertPlayer(FPlayer player){
        UUID id = UUID.randomUUID();
        return insertPlayer(id, player);
    }

    Optional<FPlayer> selectPlayerById(UUID id);

    int removePlayerById(UUID id);

    int updatePlayerById(UUID id, FPlayer player);

    List<FPlayer> selectPlayers();
}
