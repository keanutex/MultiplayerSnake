package com.levelUp.multiplayerSnake.controllers;

import com.levelUp.multiplayerSnake.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    public void updateLeaderBoard(ArrayList<String> players, ArrayList<Integer> scores){
		leaderboardService.updateScores(players, scores);
        this.template.convertAndSend("/leaderboard/updateLeaderboard",leaderboardService.getLeaderboard());
    }
	
	public void addToLeaderBoard(String playerID){
    	leaderboardService.addPlayer(playerID);
	}
	
	public void deleteFromLeaderBoard(String playerID) {
		leaderboardService.deletePlayer(playerID);
	}   
}
