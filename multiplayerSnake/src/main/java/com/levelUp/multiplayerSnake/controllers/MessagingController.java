package com.levelUp.multiplayerSnake.controllers;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class MessagingController {

    private SimpMessagingTemplate template;
    @Autowired
    public MessagingController(SimpMessagingTemplate template) {
        this.template = template;
    }


    @MessageMapping("/addMessage")
    @SendTo("/messaging/message")
    public  String addMessage( String data) throws JSONException {
        JSONObject message = new JSONObject(data);
        return message.getString("userName") + ": " + message.getString("message");
    }



}
