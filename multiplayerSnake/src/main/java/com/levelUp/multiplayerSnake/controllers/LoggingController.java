package com.levelUp.multiplayerSnake.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("loggingRes")
public class LoggingController {

    private ArrayList<String> logs = new ArrayList<String>();

    @MessageMapping("/logging")
    @SendTo("/loggingRes/moveSnakes")
    public ArrayList<String> getLogging(){
        return logs;
    }



}
