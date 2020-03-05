package com.levelUp.multiplayerSnake.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class LoggingController {

    @MessageMapping("/logging/loggingDetails")
    @SendTo("/logging/logs")
    public String getLogging(String logMessage){
        System.out.println("Sending " + logMessage);
       return  logMessage;
    }




}
