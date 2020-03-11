package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.Services.SnakeService;
import com.levelUp.multiplayerSnake.models.UpdatePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;


@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class SnakeController {

    @Autowired
    SnakeService snakeService;

    public boolean isRunning = false;

    @MessageMapping("/moveSnakes")
    @SendTo("/snake/moveSnakes")
    public void moveSnakes() throws InterruptedException {
        if (isRunning) {
            return;
        }
        isRunning = true;
        //gameLoop
        snakeService.gameLoop();
    }

    @MessageMapping("/newPlayer/{playerId}")
    @SendTo("/snake/newPlayer/{playerId}")
    public void insertPlayerIntoGame(@DestinationVariable String playerId, String colour) {
        snakeService.addPlayer(playerId, colour);
    }

    @MessageMapping("/snakeDetails")
    @SendTo("/snake/snakeDetails")
    public UpdatePayload getSnakeDetails() {
        return snakeService.getPayload();
    }

    @MessageMapping("{playerId}/removeSnake")
    @SendTo("/snake/{playerId}/removeSnake")
    public void removePlayer(@DestinationVariable String playerId) {
        snakeService.removePlayer(playerId);
    }

    @MessageMapping("{playerId}/setColour")
    @SendTo("/snake/{playerId}/setColour")
    public void setColour(@DestinationVariable String playerId, String colour) {
        snakeService.setColour(playerId, colour);
    }

    @MessageMapping("/{playerId}/changeDirection")
    @SendTo("/snake/{playerId}/changeDirection")
    public void snakeChangeDirection(String changeD, @DestinationVariable String playerId) {
        snakeService.snakeChangeDirection(changeD, playerId);
    }
}
