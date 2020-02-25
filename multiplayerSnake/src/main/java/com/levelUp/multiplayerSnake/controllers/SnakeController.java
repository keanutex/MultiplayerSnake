package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.Player;
import com.levelUp.multiplayerSnake.Snake;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/api/snake")
public class SnakeController {

    private int anzPlayer = 0;
    private boolean isRunning = false;
    private int sizeOfBox = 10, bestScoreOfAll = 0;
    private int bestScore = 0, idBestPlayer;
    private List<Player> players = new ArrayList<>();

    private List<Snake> snakeModels = new ArrayList<>();


    @PostMapping("/changeDirection")
    public void snakeChangeDirection(@RequestParam String changeD) {
            for(int i = 0; i < snakeModels.size(); i++){
                snakeModels.get(i).changeDirection(changeD);
            }

    }

    private void createFields() {
        snakeModels = new ArrayList<>();
        players = new ArrayList<>();
    }

    @PostMapping("/newPlayer")
    public ResponseEntity<Snake> insertPlayerIntoGame(@RequestParam String playerName) {

        playerName = playerName.replace("xHashTagx", "#");
        String[] splitString = playerName.split("#");

        String playerColor = "notSet";
        if (splitString.length > 1) {
            playerName = splitString[0];
            playerColor = "#" + splitString[1];
        }

        System.out.println(playerName + " joined Game: ");

        if (anzPlayer == 0) {
            // If firstPlayer Variables initialize
            createFields();
        }

        Snake newSnake = new Snake();

        snakeModels.add(newSnake);
//        snakeModels.get(anzPlayer).setClient(UUID.randomUUID());
//        snakeModels.get(anzPlayer).setPlayerNr(anzPlayer);
//        deathCounter(playerName, newSnake);
//
//        snakeModels.get(anzPlayer).newSnake(0, anzPlayer * 10 % 600, playerColor);

        return ResponseEntity.ok(snakeModels.get(anzPlayer++));
    }

    // game loop function
    @GetMapping("/runGame")
    public void runGame() {
        // only 1 Player activate the Game-Loop
        if (isRunning) {
            return;
        }
        isRunning = true;
        long time;

        // game loop
        while (true) {
            // reset Game
            if (!isRunning) {
                return;
            }

            time = System.currentTimeMillis();

            // move snakes in the direction they pointing
            movingSnakes();

            // collision of head with snake body or fodder
            checkCollision();

            // end game if no snakes exists
            if (snakeModels == null) {
                System.out.println("Error: Snake-List is empty");
                return;
            }

            while (System.currentTimeMillis() <= (time + 5 * sizeOfBox)) {
                // Waiting for next Step
            }
        }
    }
    // check collision | take and give score-Points
    private void checkCollision() {
//        for (Snake snake : snakeModels) {
//            boolean exit = false;
//
////            // if dead snake continue
////            if (snake.isPlayerDead()) {
////                continue;
////            }
//
//            snake.setPlayedTime(50);
//
//            // new body-blocks fodder caused
//            if (snake.getPosXHead() == snakeFodder.getPosX() && snake.getPosYHead() == snakeFodder.getPosY()) {
//                snake.setScore((int) (snake.getScore() * 1.25f) + 3);
//                snakeFodder.setNewPosition();
//                webSocket.convertAndSend("/snake/fodderOfSnake", snakeFodder);
//            }
//
//            for (SnakeModel snakeToCheck : snakeModels) {
//                // check if the snake´s head touches a part of a body apart from his own head
//                for (int bodyLength = 0; bodyLength < snakeToCheck.getLengthOfBody() - BooleanUtils.toInteger(snakeToCheck == snake); bodyLength++) {
//
//                    if (snake.getPosXHead() == snakeToCheck.getPosX().get(bodyLength) &&
//                            snake.getPosYHead() == snakeToCheck.getPosY().get(bodyLength)) {
//
//                        // the winner get 5 points apart from a snake which hit itself
//                        if (snakeToCheck != snake) {
//                            snakeToCheck.setScore(snakeToCheck.getScore() + 5);
//                        }
//
//                        // if someone died
//                        if (!snake.reduceScore()) {
//                            snake.setPlayerAlife(false);
//                            webSocket.convertAndSend("/snake/deleted",
//                                    "{\"" + "deletedPlayer\": " + snake.getPlayerNr() + "}");
//
//                            exit = true;
//                            break;
//                        }
//                    }
//                }
//                if (exit) {
//                    break;
//                }
//            }
//        }
    }

    // moving snakes in the direction
    private void movingSnakes() {
        for (int i = 0; i < anzPlayer; i++) {
            snakeModels.get(i).move();
        }
    }




}
