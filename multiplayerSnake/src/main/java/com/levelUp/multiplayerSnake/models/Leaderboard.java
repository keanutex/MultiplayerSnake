package com.levelUp.multiplayerSnake.models;

import java.util.*;
import java.util.Map.Entry;

public class Leaderboard {
	
    public HashMap<String, Integer> leaderboard = new HashMap<String, Integer>();	
    public Set<Entry<String, Integer>> sortedLeaderboard;
	
    public Set<Entry<String, Integer>> getSortedLeaderboard() {
    	return sortedLeaderboard;
    }
	public void addPlayer(String name) {
		leaderboard.put(name, 5);
		sortLeaderboard();
	}
		
	public void deletePlayer(String name) {
		leaderboard.remove(name);
	}
	
	public void updateScore(String name, int score) {
		leaderboard.put(name, score);
		sortLeaderboard();
	}
	
	public void sortLeaderboard() {
        Set<Entry<String,Integer>> entries = leaderboard.entrySet();
        List<Entry<String, Integer>> listOfEntries = new ArrayList<Entry<String, Integer>>(entries);      
        Collections.sort(listOfEntries, valueComparator);           
        LinkedHashMap<String, Integer> sortedByValue = new LinkedHashMap<String, Integer>(listOfEntries.size());        
        for(Entry<String, Integer> entry : listOfEntries){ 
        	sortedByValue.put(entry.getKey(), entry.getValue()); 
    	}
        Set<Entry<String, Integer>> entrySetSortedByValue = sortedByValue.entrySet(); 
     	
        sortedLeaderboard = entrySetSortedByValue;       
       }
	
	private Comparator<Entry<String, Integer>> valueComparator = new Comparator<Entry<String,Integer>>() { 
		@Override public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) { 
			Integer v1 = e1.getValue(); 
			Integer v2 = e2.getValue(); 
			
			return v2.compareTo(v1); } 
		};

}