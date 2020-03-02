package com.levelUp.multiplayerSnake.models;

import java.util.*;

public class Leaderboard {
	
	private ArrayList<Player> Players = new ArrayList<Player>();
	
	public void AddPlayer(String name) {
		Players.add(new Player(name));
	}
	
	public ArrayList<Player> getPlayers(){
		return this.Players;
	}
}