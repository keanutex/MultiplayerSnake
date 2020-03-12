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
        past50,
        diedToWall,
        diedToEnemy,
        diedToSelf
    };


    public String createMessage(messageTypes type, String id){

        switch(type){
            case past50:

                if(!checkInThe50(id)) {
                    thePast50.add(id);
                    return(snakeService.getsnake(id).getName() +" has a length of 50");
                }else{
                    return "";
                }

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

    private boolean checkInThe50(String ID){
        boolean found = false;
        for (String s : thePast50) {
            if(s.equals(ID)) {
                found = true;
                break;
            }
        }
        return found;
    }
    public void removeThe50(String ID){
       if (checkInThe50(ID)) {
           thePast50.remove(ID);
        }
    }
}


