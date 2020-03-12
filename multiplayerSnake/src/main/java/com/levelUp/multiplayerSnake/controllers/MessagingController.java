package com.levelUp.multiplayerSnake.controllers;


import com.levelUp.multiplayerSnake.Services.SnakeService;
import com.levelUp.multiplayerSnake.models.Message;
import com.levelUp.multiplayerSnake.models.Snake;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class MessagingController {

    @Autowired
    private SnakeService snakeService;

    @MessageMapping("/addMessage")
    @SendTo("/messaging/message")
    public  Message addMessage( String data) throws JSONException {
        JSONObject message = new JSONObject(data);
        Snake playerSnake = snakeService.getsnake(message.getString("playerId"));
        if(playerSnake!=null) {
            return new Message(message.getString("username"), message.getString("message"), playerSnake.getPlayerColour());
        }else{
            return new Message(message.getString("username"), message.getString("message"));
        }
    }



}
