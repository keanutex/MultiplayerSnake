package com.levelUp.multiplayerSnake.Services;

import com.levelUp.multiplayerSnake.controllers.LoggingController;
import com.levelUp.multiplayerSnake.models.*;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class LeaderboardService {
	
		public Leaderboard leaderboard = new Leaderboard();
		
		public void addPlayer(String name) {
			System.out.println(name);

			leaderboard.addPlayer(name);
		}
		
		public void deletePlayer(String name) {
			leaderboard.deletePlayer(name);
		}
		
		public Leaderboard getLeaderboard() {
			return leaderboard;
		}
		
		public void updateScores(ArrayList<String> players, ArrayList<Integer> scores) {
			for (int i = 0; i < players.size(); i++) {
				leaderboard.updateScore(players.get(i), scores.get(i));
			}
		}
		
		
   /* public Leaderboard getPayload() {
        Collection<Snake> values = snakes.values();
        ArrayList<Snake> snakeArrayList = new ArrayList<>(values);
        return new UpdatePayload(snakeArrayList, pickups);
        ArrayList<String> players = new ArrayList<String>(snakes.keySet());
        ArrayList<Integer> scores = new ArrayList<Integer>();
        for (var snake: values) {
            scores.add(snake.getLength());
        }
        return new UpdatePayload(snakeArrayList, pickups, players, scores);
    }*/

}
