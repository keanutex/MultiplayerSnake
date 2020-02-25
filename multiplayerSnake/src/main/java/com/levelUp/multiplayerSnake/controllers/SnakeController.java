package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.models.Snake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/api/snake")
public class SnakeController {

    @Autowired
    private SimpMessagingTemplate webSocket;

    private int numberOfPlayers = 0;
    private boolean isRunning = false;
    private int sizeOfBox = 10, bestScoreOfAll = 0;
    private int bestScore = 0, idBestPlayer;
    ArrayList<Snake> snakes = new ArrayList<>();

    @PostMapping("/changeDirection")
    public void snakeChangeDirection(@RequestParam String changeD) {
        for (Snake snake : snakes) {
            snake.changeDirection(changeD);
        }
    }

    @PostMapping("/getSnakes")
    public ArrayList<Snake> getSnakes() {
        return snakes;
    }

    @PostMapping("/newPlayer")
    public void insertPlayerIntoGame(@RequestParam String playerName) {
        numberOfPlayers++;
        snakes.add(new Snake());
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
        }
    }
    // moving snakes in the direction
    private void moveSnake() {
        for (Snake snake : snakes) {
            snake.move();
        }
    }

}
