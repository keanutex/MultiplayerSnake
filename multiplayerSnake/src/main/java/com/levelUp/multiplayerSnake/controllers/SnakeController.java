package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.models.Snake;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class SnakeController {

    private int numberOfPlayers = 0;
    private boolean isRunning = false;
    private int sizeOfBox = 10, bestScoreOfAll = 0;
    private int bestScore = 0, idBestPlayer;
    ArrayList<Snake> snakes = new ArrayList<>();
    Snake firstSnake = new Snake();

    @PostMapping("/changeDirection")
    public void snakeChangeDirection(@RequestParam String changeD) {
        for (Snake snake : snakes) {
            snake.changeDirection(changeD);
        }
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
    @MessageMapping("/newPlayer")
    @SendTo("/snake/newPlayer")
    public String insertPlayerIntoGame() {
        System.out.println("lmao");
        numberOfPlayers++;
        snakes.add(new Snake());
        return "newPlayerAdded";
    }


    @MessageMapping("/snakeDetails")
    @SendTo("/snake/snakeDetails")
    public ArrayList<Snake> getSnakeDetails(){
        return snakes;
    }

}
