package com.levelUp.multiplayerSnake.models;

import java.util.*;

public class Leaderboard {
	
	//private ArrayList<Player> Players = new ArrayList<Player>();
	public HashMap<String, Integer> leaderboard;
	
	
	public void addPlayer(String name) {
		leaderboard.put(name, 5);
	}
	
	public void deletePlayer(String name) {
		leaderboard.remove(name);
	}
	
	public void updateScore(String name, int score) {
		leaderboard.put(name, score);
	}
	
	private void sortLeaderboard() {
		
	}
}