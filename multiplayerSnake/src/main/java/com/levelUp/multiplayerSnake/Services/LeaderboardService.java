package com.levelUp.multiplayerSnake.Services;

import com.levelUp.multiplayerSnake.models.*;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class LeaderboardService {
	
		public Leaderboard leaderboard = new Leaderboard();
		
		public void addPlayer(String name) {
			leaderboard.addPlayer(name);
		}
		
		public void deletePlayer(String name) {
			leaderboard.deletePlayer(name);
		}
		
		public Object getLeaderboard() {
			return leaderboard.getSortedLeaderboard();
		}
		
		public void updateScores(ArrayList<String> players, ArrayList<Integer> scores) {
			for (int i = 0; i < players.size(); i++) {
				leaderboard.updateScore(players.get(i), scores.get(i));
			}
		}		
}
