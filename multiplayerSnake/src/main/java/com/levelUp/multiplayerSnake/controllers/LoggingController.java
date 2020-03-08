package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.Services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;


@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class LoggingController {

    @Autowired
    LoggingService loggingService;

    ArrayList<String> logs = new ArrayList<>();


    public void getLogging(LoggingService.messageTypes type,String name){
        logs.add(loggingService.addMessage(type,name));
        getLogs();
    }
    @MessageMapping("/loggingDetails")
    @SendTo("/logging/logs")
    public ArrayList<String> getLogs(){
        return logs;
    }


}
