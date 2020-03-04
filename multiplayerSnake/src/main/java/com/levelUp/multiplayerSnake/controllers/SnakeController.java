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
    int serverTick = 10;
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
                board.instance[snake.tail().x / 10][snake.tail().y / 10].setSnakeOccupied(false);
            }

            for (Snake snake : snakes.values()) {
                snake.speedCounter += snake.speed;
                if (snake.speedCounter >= 100 && !snake.snakeMoved) {
                    snake.snakeMoved = true;
                    snake.move();
                    snake.speedCounter = 0;
                }else{
                    snake.snakeMoved = false;
                }
                snake.directionChanged = false;
            }
            checkCollisions();

            for (Snake snake : snakes.values()) {
                board.instance[snake.head().x / 10][snake.head().y / 10].setSnakeOccupied(true);
            }

            pickupSpawnCountdown--;

            if (pickupSpawnCountdown <= 0) {
                if (pickupCounter < pickupMax) {
                    int n1 = this.generateRandomCoOrd(10, 980);
                    int n2 = this.generateRandomCoOrd(10, 980);
                    Pickup toAdd = new Pickup(n1, n2, "food");
                    pickups.add(toAdd);
                    board.instance[n1 / 10][n2 / 10].setPickupOnTile(toAdd);
                    n1 = this.generateRandomCoOrd(10, 980);
                    n2 = this.generateRandomCoOrd(10, 980);
                    toAdd = new Pickup(n1, n2, "food");
                    pickups.add(toAdd);
                    board.instance[n1 / 10][n2 / 10].setPickupOnTile(toAdd);
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
        snakes.put(playerId, new Snake(20, "up"));
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

        for (Map.Entry<String, Snake> snake : snakes.entrySet()) {
            if(!snake.getValue().snakeMoved){
                break;
            }
            if (snake.getValue().head().x / 10 < 1 || snake.getValue().head().y / 10 < 1 || snake.getValue().head().x / 10 > 98 || snake.getValue().head().y / 10 > 98) {
                keysToDelete.add(snake.getKey());
            } else {
                if (board.instance[snake.getValue().head().x / 10][snake.getValue().head().y / 10].isSnakeOccupied()) {
                    keysToDelete.add(snake.getKey());
                }
                else if (board.instance[snake.getValue().head().x / 10][snake.getValue().head().y / 10].getPickupOnTile() != null && board.instance[snake.getValue().head().x / 10][snake.getValue().head().y / 10].getPickupOnTile().type.equals("food")) {
                    snake.getValue().addSegment();
                    pickups.remove(board.instance[snake.getValue().head().x / 10][snake.getValue().head().y / 10].getPickupOnTile());
                    pickupCounter--;
                }
            }
        }
        for (int i = 0; i < keysToDelete.size(); i++) {
            Snake snakeToDelete = snakes.get(keysToDelete.get(i));
            for(SnakeSegment segment : snakeToDelete.snakeSegments){
                board.instance[segment.x/10][segment.y/10].setSnakeOccupied(false);
            }
            removePlayer(keysToDelete.get(i));
        }
    }
}
