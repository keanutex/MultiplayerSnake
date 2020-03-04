package com.levelUp.multiplayerSnake.Services;

import com.levelUp.multiplayerSnake.models.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SnakeService {


    int numberOfPlayers = 0;
    HashMap<String, Snake> snakes = new HashMap<String, Snake>();

    ArrayList<Pickup> pickups = new ArrayList<>();
    int serverTick = 20;
    int pickupSpawnCountdown = 4;
    int pickupCounter = 0;
    int pickupMax = 100;

    public void gameLoop() throws InterruptedException {
        while (true) {
            for (Snake snake : snakes.values()) {
                snake.speedCounter += snake.speed;
                if (snake.speedCounter >= 100 && !snake.snakeMoved) {
                    //snake moves
                    snake.snakeMoved = true;
                    snake.move();
                    snake.speedCounter = 0;
                    snake.directionChanged = false;
                } else {
                    //snake doesnt move
                    snake.snakeMoved = false;
                }
            }
            checkCollisions();

            //pickup spawning
            pickupSpawnCountdown--;
            if (pickupSpawnCountdown <= 0) {
                if (pickupCounter < pickupMax) {
                    int n1 = this.generateRandomCoOrd(10, 980);
                    int n2 = this.generateRandomCoOrd(10, 980);
                    Pickup toAdd = new Pickup(n1, n2, "food");
                    pickups.add(toAdd);
                    n1 = this.generateRandomCoOrd(10, 980);
                    n2 = this.generateRandomCoOrd(10, 980);
                    toAdd = new Pickup(n1, n2, "food");
                    pickups.add(toAdd);
                    pickupCounter += 2;
                    pickupSpawnCountdown = 100;
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

    public void addPlayer(String playerId, String colour) {
        numberOfPlayers++;
        snakes.put(playerId, new Snake(20, "up"));
        snakes.get(playerId).playerColour = colour;
    }

    public UpdatePayload getPayload() {
        Collection<Snake> values = snakes.values();
        ArrayList<Snake> snakeArrayList = new ArrayList<>(values);
        return new UpdatePayload(snakeArrayList, pickups);
    }

    public void removePlayer(String playerId) {
        snakes.remove(playerId);
    }

    public void setColour(String playerId, String colour) {
        snakes.get(playerId).playerColour = colour;
    }

    public void snakeChangeDirection(String changeD, String playerId) {
        if (snakes.get(playerId) == null) {
            return;
        }
        if (snakes.get(playerId).directionChanged)
            return;
        if (changeD.equals("up") && snakes.get(playerId).getDirection().equals("down"))
            return;
        if (changeD.equals("down") && snakes.get(playerId).getDirection().equals("up"))
            return;
        if (changeD.equals("left") && snakes.get(playerId).getDirection().equals("right"))
            return;
        if (changeD.equals("right") && snakes.get(playerId).getDirection().equals("left"))
            return;
        snakes.get(playerId).changeDirection(changeD);
        snakes.get(playerId).directionChanged = true;
    }

    public void checkCollisions() {
        ArrayList<String> keysToDelete = new ArrayList<>();
        ArrayList<Snake> snakesToGrow = new ArrayList<>();
        for (Map.Entry<String, Snake> snakesBase : snakes.entrySet()) {
            for (int i = 0; i < pickups.size(); i++) {
                if (snakesBase.getValue().snakeSegments.get(0).x == pickups.get(i).x && snakesBase.getValue().snakeSegments.get(0).y == pickups.get(i).y) {
                    snakesBase.getValue().addSegment();
                    pickups.remove(pickups.get(i));
                    pickupCounter--;
                }
            }
            if(snakesBase.getValue().head().x <= 0 ||  snakesBase.getValue().head().x >= 990 || snakesBase.getValue().head().y <= 0 ||  snakesBase.getValue().head().y >= 990){
                keysToDelete.add(snakesBase.getKey());
                break;
            }
            for (Map.Entry<String, Snake> snakesCheck : snakes.entrySet()) {
                for (int i = 0; i < snakesCheck.getValue().getLength(); i++) {
                    if (snakesBase.getValue().snakeSegments.get(0).equals(snakesCheck.getValue().snakeSegments.get(i))) {
                        continue;
                    }
                    if (snakesBase.getValue().head().x == snakesCheck.getValue().snakeSegments.get(i).x && snakesBase.getValue().head().y == snakesCheck.getValue().snakeSegments.get(i).y) {
                        if(!snakesBase.getKey().equals(snakesCheck.getKey())){
                            growSnakesOnCollision(snakesCheck.getValue(), snakesBase.getValue().getLength());
                        }
                        keysToDelete.add(snakesBase.getKey());
                    }
                }
            }
        }
        for (int i = 0; i < keysToDelete.size(); i++) {
            removePlayer(keysToDelete.get(i));
        }
    }

    public void growSnakesOnCollision(Snake snakeToGrow, int amount){
        for(int i = 0; i < amount; i ++){
            snakeToGrow.addSegment();
        }

    }
}
