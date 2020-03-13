package com.levelUp.multiplayerSnake.Services;

import com.levelUp.multiplayerSnake.dao.PlayerDao;
import com.levelUp.multiplayerSnake.models.FPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FSnakeService {

    private final PlayerDao playerDao;

    @Autowired
    public FSnakeService(@Qualifier("InMemory") PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    public UUID insertPlayer(FPlayer player){
        return playerDao.insertPlayer(player);
    }

    public Optional<FPlayer> selectPlayerById(UUID id){
        return playerDao.selectPlayerById(id);
    }

    public int removePlayerById(UUID id){
        return playerDao.removePlayerById(id);
    }

    public int updatePlayerById(UUID id, FPlayer player){
        return playerDao.updatePlayerById(id, player);
    }

    public List<FPlayer> selectPlayers(){
        return playerDao.selectPlayers();
    }
}
