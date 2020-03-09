package com.levelUp.multiplayerSnake.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.levelUp.multiplayerSnake.models.Message;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


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

        JSONObject asd = new JSONObject(data);
        System.out.println(asd.getString("playerID") + asd.getString("message"));
        return asd.getString("playerID") + ": " + asd.getString("message");
    }



}
