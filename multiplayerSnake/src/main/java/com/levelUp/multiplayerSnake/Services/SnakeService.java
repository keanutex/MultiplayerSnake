package com.levelUp.multiplayerSnake.Services;

import com.levelUp.multiplayerSnake.controllers.*;
import com.levelUp.multiplayerSnake.models.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SnakeService {

    @Autowired
    LoggingController loggingController;
    
    @Autowired
    LeaderboardController leaderboardController;

    int numberOfPlayers = 0;
    HashMap<String, Snake> snakes = new HashMap<>();

    ArrayList<Pickup> pickups = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Wall> walls = new ArrayList<>();

    final static int SERVER_TICK = 20;

    final static int MAP_SIZE_X = 1000;
    final static int MAP_SIZE_Y = 1000;

    //BULLETS
    final static double BULLET_SPEED_INCREMENTER = 0.995;
    final static int BULLET_LIFESPAN = 50;
    final static int BULLET_SPEED_CYCLE = 100;

    final static double SNAKE_STARTING_SPEED = 30;

    //WALLS
    final static boolean MUST_GENERATE_WALLS = true;
    final static int NUMBER_OF_WALLS_TO_GENERATE = 20;

    //PICKUPS
    double pickupSpawnCountdown = 0;
    int pickupCounter = 0;
    final static int MAXIMUM_PICKUPS = 50;
    final static int PICKUPS_PER_SPAWN = 2;
    final static double PICKUP_SPAWN_RATE = 4 * SERVER_TICK;
    final static int SUPER_FOOD_GROW_AMOUNT = 10;
    public boolean isRunning = true;

    //GAME LOOP
    final double GAME_HERTZ = 30.0;
    //Calculate how many ns each frame should take for our target game hertz.
    final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
    //At the very most we will update the game this many times before a new render.
    //If you're worried about visual hitches more than perfect timing, set this to 1.
    final int MAX_UPDATES_BEFORE_RENDER = 5;
    //We will need the last update time.
    double lastUpdateTime = System.nanoTime();
    //Store the last time we rendered.
    double lastRenderTime = System.nanoTime();

    //If we are able to get as high as this FPS, don't render again.
    final double TARGET_FPS = 60;
    final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

    //Simple way of finding FPS.
    int lastSecondTime = (int) (lastUpdateTime / 1000000000);

    public void runGameLoop() {
        if(MUST_GENERATE_WALLS){
            for(int i = 0; i < NUMBER_OF_WALLS_TO_GENERATE; i++){
                if(generateRandomNumber(0, 10) <= 5){
                    generateWallSegment(generateRandomNumber(5, 15), "vertical");
                }else{
                    generateWallSegment(generateRandomNumber(5, 15), "horizontal");
                }
            }
        }
        Thread loop = new Thread(this::gameLoop);
        loop.start();
    }



    public void gameLoop() {
        while (isRunning) {
            double now = System.nanoTime();
            int updateCount = 0;

            moveSnakes();
            moveBullets();
            checkCollisions();
            spawnPickups();

            while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                lastUpdateTime += TIME_BETWEEN_UPDATES;
                updateCount++;
            }

            if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                lastUpdateTime = now - TIME_BETWEEN_UPDATES;
            }
            lastRenderTime = now;

            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if (thisSecond > lastSecondTime) {
                lastSecondTime = thisSecond;
            }
            while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                Thread.yield();
                //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
                //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
                //FYI on some OS's this can cause pretty bad stuttering.
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                }
                now = System.nanoTime();
            }
        }
    }

    private void generateWallSegment(int size, String direction){
        switch (direction) {
            case "horizontal": {
                int randomX = generateRandomCoOrd(0, MAP_SIZE_X);
                int randomY = generateRandomCoOrd(0, MAP_SIZE_Y);
                for (int i = 0; i < size; i++) {
                    walls.add(new Wall(randomX + i * 10, randomY));
                }
                break;
            }
            case "vertical": {
                int randomX = generateRandomCoOrd(0, MAP_SIZE_X);
                int randomY = generateRandomCoOrd(0, MAP_SIZE_Y);
                for (int i = 0; i < size; i++) {
                    walls.add(new Wall(randomX, randomY + i * 10));
                }
                break;
            }
            default:
                System.out.println("direction not recognised");
                break;
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

    public int generateRandomNumber(int min, int max){
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public void addPlayer(String playerId, String stringInput) throws JSONException {
        JSONObject input = new JSONObject(stringInput);
        numberOfPlayers++;
        Snake snakeToAdd = new Snake(SNAKE_STARTING_SPEED, "up");
        snakeToAdd.setName(input.getString("playerName"));
        snakes.put(playerId, snakeToAdd);
        snakes.get(playerId).setPlayerColour(input.getString("color"));
        String playerName = snakes.get(playerId).getName();	
        leaderboardController.addToLeaderBoard(playerName);
        updateLeaderBoard();
    }

    public UpdatePayload getPayload() {
        Collection<Snake> values = snakes.values();
        ArrayList<Snake> snakeArrayList = new ArrayList<>(values);
        return new UpdatePayload(snakeArrayList, pickups, bullets, walls);
    }

    public void removePlayer(String playerId) {
        String playerName = snakes.get(playerId).getName();	
    	snakes.remove(playerId);
        leaderboardController.deleteFromLeaderBoard(playerName);
        updateLeaderBoard();
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

    public void moveSnakes() {
        for (Snake snake : snakes.values()) {
            if (snake.isSpeedBoost()) {
                snake.setBoostSpeedCounter(snake.getBoostSpeedCounter() + 1);
                if (snake.getBoostSpeedCounter() >= 400) {
                    snake.setSpeedBoost(false);
                    snake.setSpeed(snake.getBaseSpeed());
                    snake.setBoostSpeedCounter(0);
                }
            } else {
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
                //snake doesn't move
                snake.setSnakeMoved(false);
            }
        }
    }

    public void moveBullets() {
        ArrayList<Bullet> bulletsToDelete = new ArrayList<>();
        for (Bullet bullet : bullets) {
            if (bullet.getSpeedCounter() >= BULLET_SPEED_CYCLE) {
                bullet.setDeathTimer(bullet.getDeathTimer() + 1);
                if (bullet.getDeathTimer() > BULLET_LIFESPAN) {
                    bulletsToDelete.add(bullet);
                }
                bullet.move();
                bullet.setSpeedCounter(0);
            }
            bullet.setSpeedCounter(bullet.getSpeedCounter() + bullet.getSpeed());
        }

        for (int i = 0; i < bulletsToDelete.size(); i++) {
            bullets.remove(bulletsToDelete.get(i));
        }
    }

    public void spawnPickups() {
        pickupSpawnCountdown++;
        if (pickupSpawnCountdown >= PICKUP_SPAWN_RATE) {
            if (pickupCounter < MAXIMUM_PICKUPS) {
                for (int i = 0; i < PICKUPS_PER_SPAWN; i++) {
                    int random = generateRandomNumber(0, 200);
                    if (random <= 15) {
                        pickups.add(new Pickup(this.generateRandomCoOrd(10, 980), this.generateRandomCoOrd(10, 980), "SPEED"));
                    } else if (random <= 30) {
                        pickups.add(new Pickup(this.generateRandomCoOrd(10, 980), this.generateRandomCoOrd(10, 980), "SUPER_FOOD"));
                    } else {
                        pickups.add(new Pickup(this.generateRandomCoOrd(10, 980), this.generateRandomCoOrd(10, 980), "FOOD"));
                    }
                    pickupCounter++;
                }
            }
            pickupSpawnCountdown = 0;
        }
    }
    
    public void updateLeaderBoard() {
        ArrayList<String> players = new ArrayList<String>();
        ArrayList<Integer> scores = new ArrayList<Integer>();
        for (var snake: snakes.values()) {
        	players.add(snake.getName());
            scores.add(snake.getLength());
        }
       leaderboardController.updateLeaderBoard(players, scores);
    }

    public void checkCollisions() {
        ArrayList<String> keysToDelete = new ArrayList<>();

        for (Map.Entry<String, Snake> snakesBase : snakes.entrySet()) {
            //boundary collision
            if (snakesBase.getValue().head().getX() <= 0 || snakesBase.getValue().head().getX() >= (MAP_SIZE_X - 10) || snakesBase.getValue().head().getY() <= 0 || snakesBase.getValue().head().getY() >= (MAP_SIZE_Y - 10)) {
                loggingController.getLogging(LoggingService.messageTypes.diedToWall,snakesBase.getKey(),snakesBase.getValue().getPlayerColour());
                keysToDelete.add(snakesBase.getKey());
                break;
            }
            //pickup collisions
            for (int i = 0; i < pickups.size(); i++) {
                if (snakesBase.getValue().getSnakeSegments().get(0).getX() == pickups.get(i).getX() && snakesBase.getValue().getSnakeSegments().get(0).getY() == pickups.get(i).getY()) {
                	switch (pickups.get(i).getType()) {
                        case "FOOD":
                            snakesBase.getValue().setBaseSpeed(snakesBase.getValue().getBaseSpeed() * BULLET_SPEED_INCREMENTER);
                            snakesBase.getValue().addSegment();
                            pickups.remove(pickups.get(i));
                            pickupCounter--;
                        	updateLeaderBoard();
                            break;
                        case "SPEED":
                            snakesBase.getValue().setBoostSpeedCounter(0);
                            snakesBase.getValue().setSpeed(snakesBase.getValue().getSpeed() * 4);
                            snakesBase.getValue().setSpeedBoost(true);
                            pickups.remove(pickups.get(i));
                            pickupCounter--;
                            break;
                        case "SUPER_FOOD":
                            snakesBase.getValue().setBaseSpeed(snakesBase.getValue().getBaseSpeed() * Math.pow(BULLET_SPEED_INCREMENTER, SUPER_FOOD_GROW_AMOUNT));
                            for (int j = 0; j < SUPER_FOOD_GROW_AMOUNT; j++) {
                                snakesBase.getValue().addSegment();
                            }
                            pickups.remove(pickups.get(i));
                            pickupCounter--;
                        	updateLeaderBoard();
                            break;
                    }
                }
            }
            //bullet collisions
            for (Bullet bullet : bullets) {
                if (didCollide(bullet.getX(), bullet.getY(), snakesBase.getValue().head().getX(), snakesBase.getValue().head().getY(), bullet.getDir(), snakesBase.getValue().getDir())) {
                    keysToDelete.add(snakesBase.getKey());
                }
            }
            //Random wall collisions
            for(Wall wall: walls){
                if(snakesBase.getValue().head().getX() == wall.getX() && snakesBase.getValue().head().getY() == wall.getY()){
                    loggingController.getLogging(LoggingService.messageTypes.diedToWall,snakesBase.getKey(),snakesBase.getValue().getPlayerColour());
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
                        if (!snakesBase.getKey().equals(snakesCheck.getKey())) {
                            loggingController.getLogging(LoggingService.messageTypes.diedToEnemy,snakesBase.getKey(),snakesBase.getValue().getPlayerColour());
                            growSnakesOnCollision(snakesCheck.getValue(), snakesBase.getValue().getLength());
                        } else{
                            loggingController.getLogging(LoggingService.messageTypes.diedToSelf,snakesBase.getKey(),snakesBase.getValue().getPlayerColour());
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

    public void growSnakesOnCollision(Snake snakeToGrow, int amount) {
        for (int i = 0; i < amount; i++) {
            snakeToGrow.addSegment();
        }
    }

    public void fireBullet(String playerId) {
        Snake snakeThatShot = snakes.get(playerId);

        int xIncrement = 0;
        int yIncrement = 0;

        if (snakeThatShot != null) {
            snakeThatShot.setBaseSpeed(snakeThatShot.getBaseSpeed() / BULLET_SPEED_INCREMENTER);
            switch (snakeThatShot.getDirection()) {
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
            if (snakeThatShot.getLength() < 3) {
                return;
            } else {
                snakeThatShot.removeTail();
            }
            bullets.add(bullet);
            updateLeaderBoard();
        }
    }

    public boolean didCollide(int bulletX, int bulletY, int snakeX, int snakeY, String bulletDir, String snakeDir) {
        boolean collision = false;
        if (bulletX == snakeX && bulletY == snakeY) {
            return true;
        }
        switch (bulletDir) {
            case "up":
                if (snakeDir.equals("down")) {
                    if (bulletX == snakeX && (bulletY + 10) == snakeY) {
                        collision = true;
                    }
                }
                break;
            case "down":
                if (snakeDir.equals("up")) {
                    if (bulletX == snakeX && (bulletY - 10) == snakeY) {
                        collision = true;
                    }
                }
                break;
            case "left":
                if (snakeDir.equals("right")) {
                    if ((bulletX + 10) == snakeX && bulletY == snakeY) {
                        collision = true;
                    }
                }
                break;
            case "right":
                if (snakeDir.equals("left")) {
                    if ((bulletX - 10) == snakeX && (bulletY + 10) == snakeY) {
                        collision = true;
                    }
                }
                break;
        }
        return collision;
    }

    public void clearGameState(){
        snakes.clear();
        pickups.clear();
        bullets.clear();
    }

    public Snake getsnake(String playerID){
        return snakes.get(playerID);
    }
}
