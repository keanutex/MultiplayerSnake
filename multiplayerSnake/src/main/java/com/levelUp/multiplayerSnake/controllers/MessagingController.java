package com.levelUp.multiplayerSnake.controllers;


import com.levelUp.multiplayerSnake.models.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;


@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class MessagingController {



    @MessageMapping("/message")
    @SendTo("/messaging/messages")
    public  String addMessage(Message message){
        System.out.println(message.getPlayerID()  + ": " + message.getMessage());
        return message.getPlayerID() + ": " + message.getMessage();
    }
}
