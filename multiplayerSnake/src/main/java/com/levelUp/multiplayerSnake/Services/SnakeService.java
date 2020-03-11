package com.levelUp.multiplayerSnake.Services;

import com.levelUp.multiplayerSnake.models.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class SnakeService {


    int numberOfPlayers = 0;
    HashMap<String, Snake> snakes = new HashMap<String, Snake>();
    ArrayList<Pickup> pickups = new ArrayList<>();
    private ArrayList<Integer> Playerscores = new ArrayList<>(); // List for storing scores
    int serverTick = 20;
    int pickupSpawnCountdown = 4;
    int pickupCounter = 0;
    int pickupMax = 100;
    Player player;
    int foodScore=5;
    int speedScore=10;
    @Autowired
    HighscoreService highscore;

    public void gameLoop() throws InterruptedException {
        while (true) {
            for (Snake snake : snakes.values()) {
                if(snake.speedBoost){
                    snake.boostSpeedCounter += 10;
                    if(snake.boostSpeedCounter >= 4000){
                        snake.speedBoost = false;
                        snake.speed = snake.baseSpeed;
                        snake.boostSpeedCounter= 0;
                    }
                }else{
                    snake.speed = snake.baseSpeed;
                }
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
                    if(generateRandomCoOrd(0, 15) <= 1){
                        pickups.add(new Pickup(this.generateRandomCoOrd(10, 980),this.generateRandomCoOrd(10, 980), "speed"));
                    }else{
                        pickups.add(new Pickup(this.generateRandomCoOrd(10, 980), this.generateRandomCoOrd(10, 980), "food"));
                    }
                    pickups.add(new Pickup(this.generateRandomCoOrd(10, 980), this.generateRandomCoOrd(10, 980), "food"));
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
         player = new Player(playerId);
        numberOfPlayers++;
        snakes.put(playerId, new Snake(30, "up"));
        snakes.get(playerId).playerColour = colour;
    }

    public UpdatePayload getPayload() {
        Collection<Snake> values = snakes.values();
        ArrayList<Snake> snakeArrayList = new ArrayList<>(values);
        return new UpdatePayload(snakeArrayList, pickups);
    }

    public void removePlayer(String playerId) {
        numberOfPlayers--;
        snakes.remove(playerId);
        highscore.savePlayerScore(player, numberOfPlayers);
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

        for (Map.Entry<String, Snake> snakesBase : snakes.entrySet()) {
            //pickups collisions
            for (int i = 0; i < pickups.size(); i++) {
                if (snakesBase.getValue().snakeSegments.get(0).x == pickups.get(i).x && snakesBase.getValue().snakeSegments.get(0).y == pickups.get(i).y) {

                    if(pickups.get(i).type.equals("food")){
                        highscore.updatePlayerScore(player, "food");
                        snakesBase.getValue().baseSpeed *= 0.995;
                        System.out.println(snakesBase.getValue().baseSpeed);
                        snakesBase.getValue().addSegment();
                        pickups.remove(pickups.get(i));
                        pickupCounter--;
                    }else if(pickups.get(i).type.equals("speed")){
                        //highscore.savePlayerScore(player, numberOfPlayers);
                        highscore.updatePlayerScore(player, "speed");
                        snakesBase.getValue().boostSpeedCounter= 0;
                        snakesBase.getValue().speed *= 4;
                        snakesBase.getValue().speedBoost =true;
                        pickups.remove(pickups.get(i));
                        pickupCounter--;
                    }

                }
            }
            //snake collisions
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
