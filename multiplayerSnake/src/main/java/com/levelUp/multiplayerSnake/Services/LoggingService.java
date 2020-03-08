package com.levelUp.multiplayerSnake.Services;

import com.levelUp.multiplayerSnake.controllers.LoggingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
@Service
public class LoggingService {

    public enum messageTypes {
        past50,
        diedToWall,
        diedToEnemy,
        diedToSelf
    };


    public String addMessage(messageTypes type, String name){

        switch(type){
            case past50:
                return(name +" has a length of 50");
            case diedToSelf:
                return(name + " has just eaten themselves");
            case diedToWall:
                return(name + " didn't notice the wall there");
            case diedToEnemy:
                return(name + " was killed");
            default:
                return ("invalid");
        }
    }
}


