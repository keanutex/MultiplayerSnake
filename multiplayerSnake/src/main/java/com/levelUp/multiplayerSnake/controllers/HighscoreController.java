package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.Services.HighscoreService;
import com.levelUp.multiplayerSnake.models.Highscore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class HighscoreController {

    @Autowired
    HighscoreService highscoreService;

    @RequestMapping("/HighscoreDetails")
    public Highscore sendHighScore(){
        return new Highscore(highscoreService.getHighScore());
    }

}
