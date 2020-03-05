package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.Services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;


@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class LoggingController {

    @Autowired
    LoggingService loggingService;

    @MessageMapping("/loggingDetails")
    @SendTo("/logging/logs")
    public String getLogging(LoggingService.messageTypes type,String name){
        System.out.println("Sending " + name);
        return loggingService.addMessage(type,name);
    }




}
