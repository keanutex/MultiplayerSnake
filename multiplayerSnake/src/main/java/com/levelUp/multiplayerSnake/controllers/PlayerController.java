package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.models.Leaderboard;
import com.levelUp.multiplayerSnake.models.Player;
import com.levelUp.multiplayerSnake.models.Snake;
import com.levelUp.multiplayerSnake.models.UpdatePayload;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;

@Controller
public class PlayerController {

	private ArrayList<Player> Players =new ArrayList<Player>();
	int playerCount = 0;

	//@MessageMapping("/newPlayer/{playerId}")
    //@SendTo("/snake/newPlayer/{playerId}")
    public void addPlayer(@DestinationVariable String playerId, String colour) {
    	playerCount++;
    	System.out.println(playerCount);
        //snakes.put(playerId, new Snake(100));
        //snakes.get(playerId).playerColour = colour;
    }

}
