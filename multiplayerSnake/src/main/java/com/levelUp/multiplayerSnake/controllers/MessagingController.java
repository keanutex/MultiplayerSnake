package com.levelUp.multiplayerSnake.controllers;


import com.levelUp.multiplayerSnake.models.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class MessagingController {



    @MessageMapping("/message")
    @SendTo("/messaging/message")
    public  String addMessage( Message message){
        System.out.println("it worked");
        System.out.println(message.getPlayerID()  + ": " + message.getMessage());
        return message.getPlayerID() + ": " + message.getMessage();
    }
}
