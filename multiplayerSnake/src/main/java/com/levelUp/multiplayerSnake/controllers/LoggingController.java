package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.Services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private LoggingService loggingService;


    @MessageMapping("/loggingDetails")
    @SendTo("/logging/logs")
    public ArrayList<String> getLogging(){
       return  loggingService.getLogs();
    }




}
