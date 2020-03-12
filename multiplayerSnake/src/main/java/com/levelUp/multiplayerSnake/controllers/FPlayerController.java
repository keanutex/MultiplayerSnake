package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.Services.FSnakeService;
import com.levelUp.multiplayerSnake.models.FPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class FPlayerController {

    private  final FSnakeService snakeService;

    @Autowired
    public FPlayerController(FSnakeService snakeService) {
        this.snakeService = snakeService;
    }

    @MessageMapping("/players")
    @SendTo("/sync/players")
    public List<FPlayer> sendPlayers(){
        return snakeService.selectPlayers();
    }

    @MessageMapping("/join")
    @SendTo("/sync/join")
    public UUID addPlayer(FPlayer player){
        return snakeService.insertPlayer(player);
    }

    @MessageMapping("/update")
    @SendTo("/sync/players")
    public List<FPlayer> updatePlayers(FPlayer player){

        snakeService.updatePlayerById(player.getClientId(), player);
        return snakeService.selectPlayers();
    }

    @MessageMapping("/remove")
    @SendTo("/sync/players")
    public List<FPlayer> removePlayers(FPlayer player){
        snakeService.removePlayerById(player.getClientId());
        return snakeService.selectPlayers();
    }

    @MessageMapping("/player")
    @SendTo("/sync/player")
    public FPlayer getPlayer(FPlayer player){
       Optional<FPlayer> playerOptional =  snakeService.selectPlayerById(player.getClientId());
       if(playerOptional.isEmpty()){
           return null;
       }

       return playerOptional.get();
    }

}
