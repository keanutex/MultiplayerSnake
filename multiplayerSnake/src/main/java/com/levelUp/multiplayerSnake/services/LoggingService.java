package com.levelUp.multiplayerSnake.Services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class LoggingService {
    public enum messageTypes {
        past50,
        diedToWall,
        diedToEnemy,
        diedToSelf
    };

    private ArrayList<String> logs = new ArrayList<>();


    public ArrayList<String> getLogs(){
        return logs;
    }


    public void addMessage(messageTypes type, String name){
        switch(type){
            case past50:
                logs.add(name +" has a length of 50");
                break;
            case diedToSelf:
                logs.add(name + " has just eaten themselves");
                break;
            case diedToWall:
                logs.add(name + " didn't notice the wall there");
                break;
            case diedToEnemy:
                logs.add(name + " was killed");
                break;
        }
    }
}


