package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.Services.LoggingService;
import com.levelUp.multiplayerSnake.models.LoggingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;


@Controller
public class LoggingController {

    @Autowired
    LoggingService loggingService;

    private SimpMessagingTemplate template;
    @Autowired
    public LoggingController(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void getLogging(LoggingService.messageTypes type,String id, String colour){
        this.template.convertAndSend("/logging/loggingDetails",new LoggingMessage(loggingService.createMessage(type,id),colour));
    }
}
