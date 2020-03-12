package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.Services.*;
import com.levelUp.multiplayerSnake.models.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class LeaderboardController {
	
    @Autowired
    SnakeService snakeService;
    
    @Autowired
    LeaderboardService leaderboardService;
    
    private SimpMessagingTemplate template;
    @Autowired
    public LeaderboardController(SimpMessagingTemplate template) {
        this.template = template;
    }

/*
    @RequestMapping(path="/loggingDetails")
    public void getLogging(LoggingService.messageTypes type,String name){
        this.template.convertAndSend("/logging/loggingDetails",loggingService.addMessage(type,name));
    }
    private SimpMessagingTemplate template;*/

    public void updateLeaderBoard(ArrayList<String> players, ArrayList<Integer> scores){
		System.out.println("Updating Leaderboard");
		leaderboardService.updateScores(players, scores);

        this.template.convertAndSend("/leaderboard/updateLeaderboard",leaderboardService.getLeaderboard());
    }
	
   /* @MessageMapping("/updateLeaderboard")
    @SendTo("/leaderboard/updateLeaderboard")
	public Leaderboard updateLeaderBoard(){
		//Collections.sort(leaderboard.getPlayers());
		//sort method based on leader scores
		System.out.println("Updating Leaderboard");
		return leaderboardService.getLeaderboard();
	}*/
    
    //@MessageMapping("/addToLeaderboard/{playerID}")
   // @SendTo("/leaderboard/updateLeaderboard")
	public void addToLeaderBoard(String playerID){
    	//updateLeaderBoard();
		System.out.println(playerID);

    	leaderboardService.addPlayer(playerID);
    	//DO A CALL TO SORT THE LEADERBOARD
    	//leaderboardService.addPlayer(playerID);
		System.out.println("Adding to Leaderboard");
		//return leaderboardService.getLeaderboard();
	}
	
	public void deleteFromLeaderBoard(String playerID) {
		leaderboardService.deletePlayer(playerID);
	}
    

   /* @MessageMapping("/newPlayer/{playerId}")
    @SendTo("/snake/newPlayer/{playerId}")
    public void insertPlayerIntoGame(@DestinationVariable String playerId, String colour) {
        snakeService.addPlayer(playerId, colour);
    }*/
    

}
