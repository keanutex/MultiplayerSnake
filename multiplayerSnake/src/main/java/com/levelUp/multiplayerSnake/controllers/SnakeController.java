package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.models.Snake;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class SnakeController {

    int numberOfPlayers = 0;
    ArrayList<Snake> snakes = new ArrayList<>(); //TODO CHANGE TO MAP WHERE PLAYERID IS THE KEY
    public boolean isRunning = false;

    //add pickups


    @MessageMapping("/moveSnakes")
    @SendTo("/snake/moveSnakes")
    public void moveSnakes() throws InterruptedException {
        if(isRunning){
            return;
        }
        isRunning = true;
        while(true){
            for(int i = 0; i < snakes.size(); i++){
                snakes.get(i).move();
            }
            Thread.sleep(1000);
        }
    }

    @MessageMapping("/newPlayer")
    @SendTo("/snake/newPlayer")
    public String insertPlayerIntoGame() {
        numberOfPlayers++;
        snakes.add(new Snake());
        return "newPlayerAdded";
    }

    @MessageMapping("/snakeDetails")
    @SendTo("/snake/snakeDetails")
    public ArrayList<Snake> getSnakeDetails(){
        return snakes;
    }

    @MessageMapping("/changeDirection")
    @SendTo("/snake/changeDirection")
    public void snakeChangeDirection(String changeD) { //add unique identifier for snake
        for(int i = 0; i < snakes.size(); i++){
            //if(snakes.get(i).playerName.equals(playerId)){
            if(changeD.equals("up") && snakes.get(i).getDirection().equals("down"))
                return;
            if(changeD.equals("down") && snakes.get(i).getDirection().equals("up"))
                return;
            if(changeD.equals("left") && snakes.get(i).getDirection().equals("right"))
                return;
            if(changeD.equals("right") && snakes.get(i).getDirection().equals("left"))
                return;

            snakes.get(i).changeDirection(changeD);
            //}
        }
    }
}
