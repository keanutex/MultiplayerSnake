package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.models.Pickup;
import com.levelUp.multiplayerSnake.models.Snake;
import com.levelUp.multiplayerSnake.models.SnakeSegment;
import com.levelUp.multiplayerSnake.models.UpdatePayload;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class SnakeController {

    int numberOfPlayers = 0;
    HashMap<String, Snake> snakes = new HashMap<String, Snake>();
    public boolean isRunning = false;
    ArrayList<Pickup> pickups = new ArrayList<>();

    @MessageMapping("/moveSnakes")
    @SendTo("/snake/moveSnakes")
    public void moveSnakes() throws InterruptedException {
        if(isRunning){
            return;
        }

        isRunning = true;
        pickups.add(new Pickup(100, 100));
        pickups.add(new Pickup(50, 100));
        pickups.add(new Pickup(100, 200));
        //gameLoop
        while(true){
            for (Snake snake: snakes.values()) {
                snake.move();
            }
            checkCollisions();
            Thread.sleep(1000);
        }
    }

    @MessageMapping("/newPlayer/{playerId}")
    @SendTo("/snake/newPlayer/{playerId}")
    public String insertPlayerIntoGame(@DestinationVariable String playerId, String colour) {
        numberOfPlayers++;
        snakes.put(playerId, new Snake());
        snakes.get(playerId).playerColour = colour;
        return "newPlayerAdded";
    }

    @MessageMapping("/snakeDetails")
    @SendTo("/snake/snakeDetails")
    public UpdatePayload getSnakeDetails(){
        Collection<Snake> values = snakes.values();
        ArrayList<Snake> snakeArrayList = new ArrayList<>(values);
        return new UpdatePayload(snakeArrayList, pickups);
    }

    @MessageMapping("{playerId}/removeSnake")
    @SendTo("/snake/{playerId}/removeSnake")
    public void removePlayer( @DestinationVariable String playerId){
        snakes.remove(playerId);
    }

    @MessageMapping("{playerId}/setColour")
    @SendTo("/snake/{playerId}/setColour")
    public void setColour( @DestinationVariable String playerId, String colour){
        System.out.println(colour);
        snakes.get(playerId).playerColour = colour;
    }

    @MessageMapping("/{playerId}/changeDirection")
    @SendTo("/snake/{playerId}/changeDirection")
    public void snakeChangeDirection(String changeD, @DestinationVariable String playerId) {
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

    public void checkCollisions(){
        for (Map.Entry<String, Snake> snakesBase : snakes.entrySet()) {
            for(Map.Entry<String, Snake> snakesCheck : snakes.entrySet()){
                for(SnakeSegment snakeSegments : snakesCheck.getValue().snakeSegments){
                    if(snakesBase.getValue().snakeSegments.get(0).equals(snakeSegments)){
                        continue;
                    }
                        if(snakesBase.getValue().snakeSegments.get(0).x == snakeSegments.x && snakesBase.getValue().snakeSegments.get(0).y == snakeSegments.y){
                            System.out.println("COLLISION AT: " + snakesBase.getValue().snakeSegments.get(0).x + ", " + snakesBase.getValue().snakeSegments.get(0).y
                            + "\n BETWEEN: " + snakesBase.getKey() + " AND " + snakesCheck.getKey());
                            removePlayer(snakesBase.getKey());
                            //Disconnect player maybe
                    }
                }
            }
        }

        for (Map.Entry<String, Snake> snakesBase : snakes.entrySet()) {
            int index = 0;
            for(Pickup pickup : pickups){
                index++;
                    if(snakesBase.getValue().snakeSegments.get(0).x == pickup.x && snakesBase.getValue().snakeSegments.get(0).y == pickup.y){
                        System.out.println("ATE A PICKUP: " + snakesBase.getValue().snakeSegments.get(0).x + ", " + snakesBase.getValue().snakeSegments.get(0).y
                                + "\n SNAKE: " + snakesBase.getKey());
                        snakesBase.getValue().addSegment();
                    pickups.remove(pickup);
                }
            }
        }
    }
}
