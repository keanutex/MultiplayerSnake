package com.levelUp.multiplayerSnake.Services;

import com.levelUp.multiplayerSnake.controllers.LoggingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class LoggingService {

    @Autowired
    private LoggingController lc;
    public enum messageTypes {
        past50,
        diedToWall,
        diedToEnemy,
        diedToSelf
    };


    public void addMessage(messageTypes type, String name){

        switch(type){
            case past50:
                lc.getLogging(name +" has a length of 50");
                break;
            case diedToSelf:
                lc.getLogging(name + " has just eaten themselves");
                break;
            case diedToWall:
                System.out.println(lc);
                lc.getLogging(name + " didn't notice the wall there");
                break;
            case diedToEnemy:
                lc.getLogging(name + " was killed");
                break;
        }
    }
}


