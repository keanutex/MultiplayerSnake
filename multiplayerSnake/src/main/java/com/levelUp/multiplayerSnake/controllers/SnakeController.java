package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.models.Snake;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class SnakeController {

    int numberOfPlayers = 0;
    HashMap<String, Snake> snakes = new HashMap<String, Snake>();
    public boolean isRunning = false;

    @MessageMapping("/moveSnakes")
    @SendTo("/snake/moveSnakes")
    public void moveSnakes() throws InterruptedException {
        if(isRunning){
            return;
        }
        isRunning = true;
        while(true){
            for (Snake snake: snakes.values()) {
                snake.move();
            }
            Thread.sleep(1000);
        }
    }

    @MessageMapping("/newPlayer/{playerId}")
    @SendTo("/snake/newPlayer/{playerId}")
    public String insertPlayerIntoGame(@DestinationVariable String playerId) {
        numberOfPlayers++;
        snakes.put(playerId, new Snake()); //TODO ADD SNAKE
        return "newPlayerAdded";
    }

    @MessageMapping("/snakeDetails")
    @SendTo("/snake/snakeDetails")
    public ArrayList<Snake> getSnakeDetails(){
        Collection<Snake> values = snakes.values();
        return new ArrayList<>(values);
    }

    @MessageMapping("/removeSnake/{playerId}")
    @SendTo("/snake/removeSnake/{playerId}")
    public void removePlayer( @DestinationVariable String playerId){
        System.out.println("player removed " + playerId);
        snakes.remove(playerId);
    }


    @MessageMapping("/{playerId}/changeDirection")
    @SendTo("/snake/{playerId}/changeDirection")
    public void snakeChangeDirection(String changeD, @DestinationVariable String playerId) { //add unique identifier for snake

            if(changeD.equals("up") && snakes.get(playerId).getDirection().equals("down"))
                return;
            if(changeD.equals("down") && snakes.get(playerId).getDirection().equals("up"))
                return;
            if(changeD.equals("left") && snakes.get(playerId).getDirection().equals("right"))
                return;
            if(changeD.equals("right") && snakes.get(playerId).getDirection().equals("left"))
                return;

            snakes.get(playerId).changeDirection(changeD);

    }
}
