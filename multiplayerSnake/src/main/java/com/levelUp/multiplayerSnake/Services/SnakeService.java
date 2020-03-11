package com.levelUp.multiplayerSnake.Services;

import com.levelUp.multiplayerSnake.models.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SnakeService {


    int numberOfPlayers = 0;
    HashMap<String, Snake> snakes = new HashMap<>();

    ArrayList<Pickup> pickups = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();

    final static int SERVER_TICK = 20;


    final static double BULLET_SPEED_INCREMENTER = 0.995;
    final static int BULLET_LIFESPAN = 50;
    final static int BULLET_SPEED_CYCLE = 100;

    final static double SNAKE_STARTING_SPEED = 30;

    //PICKUPS
    double pickupSpawnCountdown = 0;
    int pickupCounter = 0;
    final static int MAXIMUM_PICKUPS = 100;
    final static int PICKUPS_PER_SPAWN = 2;
    final static double PICKUP_SPAWN_RATE = 4*SERVER_TICK;

    public void gameLoop() throws InterruptedException {
        while (true) {
            for (Snake snake : snakes.values()) {
                if(snake.isSpeedBoost()){
                    snake.setBoostSpeedCounter(snake.getBoostSpeedCounter() + 1);
                    if(snake.getBoostSpeedCounter() >= 400){
                        snake.setSpeedBoost(false);
                        snake.setSpeed(snake.getBaseSpeed());
                        snake.setBoostSpeedCounter(0);
                    }
                }else{
                    snake.setSpeed(snake.getBaseSpeed());
                }
                snake.setSpeedCounter(snake.getSpeedCounter() + snake.getSpeed());
                if (snake.getSpeedCounter() >= 100 && !snake.isSnakeMoved()) {
                    //snake moves
                    snake.setSnakeMoved(true);
                    snake.move();
                    snake.setSpeedCounter(0);
                    snake.setDirectionChanged(false);
                } else {
                    //snake doesnt move
                    snake.setSnakeMoved(false);
                }
            }
            ArrayList<Bullet> bulletsToDelete = new ArrayList<>();
            for(Bullet bullet : bullets){
                if(bullet.getSpeedCounter() >= BULLET_SPEED_CYCLE){
                    bullet.setDeathTimer(bullet.getDeathTimer() + 1);
                    if(bullet.getDeathTimer() > BULLET_LIFESPAN){
                        bulletsToDelete.add(bullet);
                    }
                    bullet.move();
                    bullet.setSpeedCounter(0);
                }
                bullet.setSpeedCounter(bullet.getSpeedCounter() + bullet.getSpeed());
            }

            for(int i = 0; i < bulletsToDelete.size(); i++){
                bullets.remove(bulletsToDelete.get(i));
            }
            checkCollisions();

            //pickup spawning
            pickupSpawnCountdown++;
            if (pickupSpawnCountdown >= PICKUP_SPAWN_RATE) {
                if (pickupCounter < MAXIMUM_PICKUPS) {
                    for(int i = 0; i < PICKUPS_PER_SPAWN; i++){
                        int random = generateRandomCoOrd(0, 150);
                        if(random <= 0){
                            pickups.add(new Pickup(this.generateRandomCoOrd(10, 980),this.generateRandomCoOrd(10, 980), "speed"));
                        }else if (random <= 20){
                            pickups.add(new Pickup(this.generateRandomCoOrd(10, 980), this.generateRandomCoOrd(10, 980), "shoot"));
                        }else{
                            pickups.add(new Pickup(this.generateRandomCoOrd(10, 980), this.generateRandomCoOrd(10, 980), "food"));
                        }
                        pickupCounter ++;
                    }
                }
                pickupSpawnCountdown = 0;
            }
            Thread.sleep(SERVER_TICK);
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
        snakes.put(playerId, new Snake(SNAKE_STARTING_SPEED, "up"));
        snakes.get(playerId).setPlayerColour(colour);
    }

    public UpdatePayload getPayload() {
        Collection<Snake> values = snakes.values();
        ArrayList<Snake> snakeArrayList = new ArrayList<>(values);
        return new UpdatePayload(snakeArrayList, pickups, bullets);
    }

    public void removePlayer(String playerId) {
        snakes.remove(playerId);
    }

    public void setColour(String playerId, String colour) {
        snakes.get(playerId).setPlayerColour(colour);
    }

    public void snakeChangeDirection(String changeD, String playerId) {
        if (snakes.get(playerId) == null) {
            return;
        }
        if (snakes.get(playerId).isDirectionChanged())
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
        snakes.get(playerId).setDirectionChanged(true);
    }

    public void checkCollisions() {
        ArrayList<String> keysToDelete = new ArrayList<>();

        //boundary collision
        for (Map.Entry<String, Snake> snakesBase : snakes.entrySet()) {
            if(snakesBase.getValue().head().getX() <= 0 ||  snakesBase.getValue().head().getX() >= 990 || snakesBase.getValue().head().getY() <= 0 ||  snakesBase.getValue().head().getY() >= 990){
                keysToDelete.add(snakesBase.getKey());
                break;
            }

            //pickup collisions
            for (int i = 0; i < pickups.size(); i++) {
                if (snakesBase.getValue().getSnakeSegments().get(0).getX() == pickups.get(i).getX() && snakesBase.getValue().getSnakeSegments().get(0).getY() == pickups.get(i).getY()) {
                    if(pickups.get(i).getType().equals("food")){
                        snakesBase.getValue().setBaseSpeed(snakesBase.getValue().getBaseSpeed() * BULLET_SPEED_INCREMENTER);
                        System.out.println(snakesBase.getValue().getBaseSpeed());
                        snakesBase.getValue().addSegment();
                        pickups.remove(pickups.get(i));
                        pickupCounter--;
                    }else if(pickups.get(i).getType().equals("speed")){
                        snakesBase.getValue().setBoostSpeedCounter(0);
                        snakesBase.getValue().setSpeed( snakesBase.getValue().getSpeed() * 4);
                        snakesBase.getValue().setSpeedBoost(true);
                        pickups.remove(pickups.get(i));
                        pickupCounter--;
                    }
                }
            }
            for(Bullet bullet: bullets){
                if(didCollide(bullet.getX(), bullet.getY(), snakesBase.getValue().head().getX(), snakesBase.getValue().head().getY(), bullet.getDir(), snakesBase.getValue().getDir())){
                    keysToDelete.add(snakesBase.getKey());
                }
            }
            //snake on snake collisions
            for (Map.Entry<String, Snake> snakesCheck : snakes.entrySet()) {
                for (int i = 0; i < snakesCheck.getValue().getLength(); i++) {
                    if (snakesBase.getValue().getSnakeSegments().get(0).equals(snakesCheck.getValue().getSnakeSegments().get(i))) {
                        continue;
                    }
                    if (snakesBase.getValue().head().getX() == snakesCheck.getValue().getSnakeSegments().get(i).getX() && snakesBase.getValue().head().getY() == snakesCheck.getValue().getSnakeSegments().get(i).getY()) {
                        if(!snakesBase.getKey().equals(snakesCheck.getKey())){
                            growSnakesOnCollision(snakesCheck.getValue(), snakesBase.getValue().getLength());
                        }
                        keysToDelete.add(snakesBase.getKey());
                    }
                }
            }
        }

        //deleting the snakes that collided
        for (int i = 0; i < keysToDelete.size(); i++) {
            removePlayer(keysToDelete.get(i));
        }
    }

    public void growSnakesOnCollision(Snake snakeToGrow, int amount){
        for(int i = 0; i < amount; i ++){
            snakeToGrow.addSegment();
        }
    }

    public void fireBullet(String playerId){
        Snake snakeThatShot = snakes.get(playerId);

        int xIncrement = 0;
        int yIncrement = 0;

        if(snakeThatShot !=null){
            snakeThatShot.setBaseSpeed(snakeThatShot.getBaseSpeed() / BULLET_SPEED_INCREMENTER);
            switch(snakeThatShot.getDirection()){
                case "up":
                    yIncrement = -20;
                    break;
                case "down":
                    yIncrement = 20;
                    break;
                case "left":
                    xIncrement = -20;
                    break;
                case "right":
                    xIncrement = 20;
                    break;
                default:
                    System.out.println("direction not recognised");
            }

            Bullet bullet = new Bullet(snakeThatShot.head().getX() + xIncrement, snakeThatShot.head().getY() + yIncrement, snakeThatShot.getDirection(), 50);
            if(snakeThatShot.getLength()  < 3){
                return;
            }else{
                snakeThatShot.removeTail();
            }
            bullets.add(bullet);
        }
    }

    public boolean didCollide(int bulletX, int bulletY, int snakeX, int snakeY, String bulletDir, String snakeDir){
        boolean collision = false;
        if(bulletX == snakeX && bulletY == snakeY){
           return true;
        }
        switch (bulletDir){
            case "up":
                if(snakeDir.equals("down") ){
                    if(bulletX == snakeX && (bulletY + 10) == snakeY){
                        collision = true;
                    }
                }
                break;
            case "down":
                if(snakeDir.equals("up")){
                    if(bulletX == snakeX && (bulletY - 10) == snakeY){
                        collision = true;
                    }
                }
                break;
            case "left":
                if(snakeDir.equals("right")){
                    if((bulletX+10) == snakeX && bulletY == snakeY){
                        collision = true;
                    }
                }
                break;
            case "right":
                if(snakeDir.equals("left")){
                    if((bulletX-10) == snakeX && (bulletY + 10) == snakeY){
                        collision = true;
                    }
                }
                break;
        }
        return collision;
    }
}
