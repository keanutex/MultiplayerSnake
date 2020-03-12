package com.levelUp.multiplayerSnake.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LoggingService {

    ArrayList<String> thePast50 = new ArrayList<String>();
    @Autowired
    SnakeService snakeService;

    public enum messageTypes {
        diedToWall,
        diedToEnemy,
        diedToSelf
    };


    public String createMessage(messageTypes type, String id){

        switch(type){
            case diedToSelf:
                return(snakeService.getsnake(id).getName() + " has just eaten themselves");
            case diedToWall:
                return(snakeService.getsnake(id).getName() + " didn't notice the wall there");
            case diedToEnemy:
                return(snakeService.getsnake(id).getName() + " was killed");
            default:
                return ("invalid");
        }
    }
}


