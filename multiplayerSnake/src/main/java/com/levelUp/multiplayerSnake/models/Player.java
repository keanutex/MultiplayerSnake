package com.levelUp.multiplayerSnake.models;
import java.util.*;

public class Player {
	
	private String name;
	private int scoreCur;
	private int scoreMax;
	private ArrayList<Integer> score = new ArrayList<>();
	
	public Player(String name) {
		
		this.name = name;
		this.scoreCur = 0;
		this.scoreMax = 0;
	}
	public String getName()
	{
		return this.name;
	}
	
	public int getScoreCur() {
		return this.scoreCur;
	}
	
	public int getScoreMax() {
		return this.scoreMax;
	}

	public void setScoreCur(int score){
		scoreCur+=score;
	}

	public void setScoreMax(int score){
		scoreMax+=score;
	}

	
}