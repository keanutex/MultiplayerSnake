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

import java.util.*;


@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class SnakeController {

    int numberOfPlayers = 0;
    HashMap<String, Snake> snakes = new HashMap<String, Snake>();
    public boolean isRunning = false;
    ArrayList<Pickup> pickups = new ArrayList<>();
    int serverTick = 100;
    int pickupSpawnCountdown = 4;

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
                snake.speedCounter += snake.speed;
                if(snake.speedCounter >= 100){
                    snake.move();
                    snake.speedCounter = 0;
                }
                snake.directionChanged = false;

            }
            checkCollisions();

            pickupSpawnCountdown--;

            if(pickupSpawnCountdown<= 0){
                pickups.add(new Pickup(this.generateRandomCoOrd(1000), this.generateRandomCoOrd(1000)));
                pickups.add(new Pickup(this.generateRandomCoOrd(1000), this.generateRandomCoOrd(1000)));
                pickups.add(new Pickup(this.generateRandomCoOrd(1000), this.generateRandomCoOrd(1000)));
                pickupSpawnCountdown = 4;
            }


            Thread.sleep(serverTick);
        }
    }

    public int generateRandomCoOrd(int bound){
        int n = new Random().nextInt(bound);
        n = (int) (Math.round(n/10.0) * 10);
        return n;
    }

    @MessageMapping("/newPlayer/{playerId}")
    @SendTo("/snake/newPlayer/{playerId}")
    public void insertPlayerIntoGame(@DestinationVariable String playerId, String colour) {
        numberOfPlayers++;
        snakes.put(playerId, new Snake(100));
        snakes.get(playerId).playerColour = colour;
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
            if(snakes.get(playerId).directionChanged)
                return;
            snakes.get(playerId).directionChanged = true;
            snakes.get(playerId).changeDirection(changeD);
    }

    public void checkCollisions(){
        //TODO NEEDS TO CHANGE TO AN ITERATOR (CANT DELETE OBJECT BEING LOOPED THROUGH)
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
            for(int i = 0; i < pickups.size(); i++){
                    if(snakesBase.getValue().snakeSegments.get(0).x == pickups.get(i).x && snakesBase.getValue().snakeSegments.get(0).y == pickups.get(i).y){
                        System.out.println("ATE A PICKUP: " + snakesBase.getValue().snakeSegments.get(0).x + ", " + snakesBase.getValue().snakeSegments.get(0).y
                                + "\n SNAKE: " + snakesBase.getKey());
                        snakesBase.getValue().addSegment();
                    pickups.remove(pickups.get(i));
                }
            }
        }
    }
}
