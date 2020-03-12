package com.levelUp.multiplayerSnake.dao;

import com.levelUp.multiplayerSnake.models.FPlayer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("InMemory")
public class PlayerDas implements PlayerDao {

    private static List<FPlayer> DB = new ArrayList<>();

    @Override
    public UUID insertPlayer(UUID id, FPlayer player) {

        DB.add(new FPlayer(id, player.getColor(),
                player.getFPosition(), player.getScore()));

        return id;
    }

    @Override
    public Optional<FPlayer> selectPlayerById(UUID id) {

        return DB.stream()
                .filter(player -> player.getClientId().equals(id))
                .findFirst();
    }

    @Override
    public int removePlayerById(UUID id) {

        Optional<FPlayer> playerOptional = selectPlayerById(id);
        if(playerOptional.isEmpty()){
            return 0;
        }
        DB.remove(playerOptional.get());
        return 1;
    }

    @Override
    public int updatePlayerById(UUID id, FPlayer player) {

        Optional<FPlayer> playerOptional = selectPlayerById(id);

        if(playerOptional.isEmpty()){
            return 0;
        }
        else {

            int index = DB.indexOf(playerOptional.get());

            if(index >= 0){
                DB.set(index, new FPlayer(id, player.getColor(), player.getFPosition(), player.getScore()));
                return 1;
            }
        }

        return 0;
    }

    @Override
    public List<FPlayer> selectPlayers() {
        return DB;
    }

}
