package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.models.*;
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
    int pickupCounter = 0;
    int pickupMax = 100;
    Board board = new Board();

    @MessageMapping("/moveSnakes")
    @SendTo("/snake/moveSnakes")
    public void moveSnakes() throws InterruptedException {
        if (isRunning) {
            return;
        }
        isRunning = true;
        //gameLoop
        while (true) {
            for (Snake snake : snakes.values()) {
                snake.speedCounter += snake.speed;
                if (snake.speedCounter >= 100) {
                    snake.move();
                    snake.speedCounter = 0;
                }
                snake.directionChanged = false;
            }
            checkCollisions();
            pickupSpawnCountdown--;

            if (pickupSpawnCountdown <= 0) {
                if (pickupCounter < pickupMax) {
                    pickups.add(new Pickup(this.generateRandomCoOrd(10, 980), this.generateRandomCoOrd(10, 980), "food"));
                    pickups.add(new Pickup(this.generateRandomCoOrd(10, 980), this.generateRandomCoOrd(10, 980), "food"));
                    pickups.add(new Pickup(this.generateRandomCoOrd(10, 980), this.generateRandomCoOrd(10, 980), "food"));
                    pickupCounter += 3;
                    pickupSpawnCountdown = 10;
                }
            }
            Thread.sleep(serverTick);
        }
    }

    public int generateRandomCoOrd(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min;
        number = (int) (Math.round(number / 10.0) * 10);
        return number;
    }

    @MessageMapping("/newPlayer/{playerId}")
    @SendTo("/snake/newPlayer/{playerId}")
    public void insertPlayerIntoGame(@DestinationVariable String playerId, String colour) {
        numberOfPlayers++;
        snakes.put(playerId, new Snake(100, "up"));
        snakes.get(playerId).playerColour = colour;
    }

    @MessageMapping("/snakeDetails")
    @SendTo("/snake/snakeDetails")
    public UpdatePayload getSnakeDetails() {
        Collection<Snake> values = snakes.values();
        ArrayList<Snake> snakeArrayList = new ArrayList<>(values);
        return new UpdatePayload(snakeArrayList, pickups);
    }

    @MessageMapping("{playerId}/removeSnake")
    @SendTo("/snake/{playerId}/removeSnake")
    public void removePlayer(@DestinationVariable String playerId) {
        snakes.remove(playerId);
    }

    @MessageMapping("{playerId}/setColour")
    @SendTo("/snake/{playerId}/setColour")
    public void setColour(@DestinationVariable String playerId, String colour) {
        snakes.get(playerId).playerColour = colour;
    }

    @MessageMapping("/{playerId}/changeDirection")
    @SendTo("/snake/{playerId}/changeDirection")
    public void snakeChangeDirection(String changeD, @DestinationVariable String playerId) {
        if (snakes.get(playerId) == null) {
            return;
        }
        if (changeD.equals("up") && snakes.get(playerId).getDirection().equals("down"))
            return;
        if (changeD.equals("down") && snakes.get(playerId).getDirection().equals("up"))
            return;
        if (changeD.equals("left") && snakes.get(playerId).getDirection().equals("right"))
            return;
        if (changeD.equals("right") && snakes.get(playerId).getDirection().equals("left"))
            return;
        if (snakes.get(playerId).directionChanged)
            return;
        snakes.get(playerId).directionChanged = true;
        snakes.get(playerId).changeDirection(changeD);
    }

    public void checkCollisions() {
        ArrayList<String> keysToDelete = new ArrayList<>();
        for (Map.Entry<String, Snake> snakesBase : snakes.entrySet()) {
            if(snakesBase.getValue().head().x <= 0 ||  snakesBase.getValue().head().x >= 990 || snakesBase.getValue().head().y <= 0 ||  snakesBase.getValue().head().y >= 990)
                keysToDelete.add(snakesBase.getKey());
            for (Map.Entry<String, Snake> snakesCheck : snakes.entrySet()) {
                for (SnakeSegment snakeSegments : snakesCheck.getValue().snakeSegments) {
                    if (snakesBase.getValue().head().equals(snakeSegments)) {
                        continue;
                    }
                    if (snakesBase.getValue().head().x == snakeSegments.x && snakesBase.getValue().head().y == snakeSegments.y) {
                        keysToDelete.add(snakesBase.getKey());
                    }
                }
            }
        }
        for (int i = 0; i < keysToDelete.size(); i++) {
            //disconnect players in this array
            removePlayer(keysToDelete.get(i));
        }

        for (Map.Entry<String, Snake> snakesBase : snakes.entrySet()) {
            for (int i = 0; i < pickups.size(); i++) {
                if (snakesBase.getValue().head().x == pickups.get(i).x && snakesBase.getValue().head().y == pickups.get(i).y) {
                    snakesBase.getValue().addSegment();
                    pickups.remove(pickups.get(i));
                    pickupCounter--;
                }
            }
        }
    }
}
