package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.services.LoggingService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
    private AnnotationConfigApplicationContext context;
    private LoggingService loggingService;
public LoggingController(){
    context = new AnnotationConfigApplicationContext();
    context.scan("com.levelUp.multiplayerSnake");
    context.refresh();

    loggingService = context.getBean(LoggingService.class);
}

    @MessageMapping("/logging")
    @SendTo("/loggingRes/moveSnakes")
    public ArrayList<String> getLogging(){
       return  loggingService.getLogs();

    }




}
