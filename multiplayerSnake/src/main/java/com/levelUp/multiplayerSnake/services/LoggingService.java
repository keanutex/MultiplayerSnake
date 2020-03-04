package com.levelUp.multiplayerSnake.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("ls")
public class LoggingService {
    public enum messageTypes {
        past100,
        diedToWall,
        diedToEnemy,
        diedToSelf
    };

    private ArrayList<String> logs = new ArrayList<String>();


    public ArrayList<String> getLogs(){
        return logs;
    }


    public void addMessage(messageTypes type, String name){

    }
}


