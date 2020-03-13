package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.Services.HighscoreService;
import com.levelUp.multiplayerSnake.Services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;


@Controller
public class HighscoreController {

    @Autowired
    HighscoreService highscoreService;

    private SimpMessagingTemplate template;
    @Autowired
    public HighscoreController(SimpMessagingTemplate template) {
        this.template = template;
    }




    @RequestMapping(path="/HighscoreDetails")
    public void getLogging(LoggingService.messageTypes type,String name){
        this.template.convertAndSend("/Highscore/HighscoreDetails",highscoreService.getHighScore());
    }
}
