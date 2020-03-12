package com.levelUp.multiplayerSnake.models;

import java.util.*;
import java.util.Map.Entry;

public class Leaderboard {
	
	//private ArrayList<Player> Players = new ArrayList<Player>();
    public HashMap<String, Integer> leaderboard = new HashMap<String, Integer>();	
    public Set<Entry<String, Integer>> sortedLeaderboard;
	
    
    public Set<Entry<String, Integer>> getSortedLeaderboard() {
    	return sortedLeaderboard;
    }
	public void addPlayer(String name) {
		leaderboard.put(name, 5);
		sortLeaderboard();
	}
	
	public void addPlayer(String name, int score) {
		leaderboard.put(name, score);
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
        //****
       // System.out.println("HashMap before sorting, random order "); 
       // for(Entry<String, Integer> entry : entries){ System.out.println(entry.getKey() + " ==> " + entry.getValue()); }
//*********
        List<Entry<String, Integer>> listOfEntries = new ArrayList<Entry<String, Integer>>(entries);
       
        
        //****
      //  System.out.println("HashMap before sorting, random order "); 
    //    for(Entry<String, Integer> entry : listOfEntries){ System.out.println(entry.getKey() + " ==> " + entry.getValue()); }
//*********
        Collections.sort(listOfEntries, valueComparator);
        //****
      //  System.out.println("HashMap after sorting, random order "); 
       // for(Entry<String, Integer> entry : listOfEntries){ System.out.println(entry.getKey() + " ==> " + entry.getValue()); }
//*********
        
        
        LinkedHashMap<String, Integer> sortedByValue = new LinkedHashMap<String, Integer>(listOfEntries.size());
        
        for(Entry<String, Integer> entry : listOfEntries){ 
        	sortedByValue.put(entry.getKey(), entry.getValue()); 
    	}
        //********
     //   System.out.println("HashMap after sorting entries by values "); 
        Set<Entry<String, Integer>> entrySetSortedByValue = sortedByValue.entrySet(); 
     //   for(Entry<String, Integer> mapping : entrySetSortedByValue){ 
        //	System.out.println(mapping.getKey() + " ==> " + mapping.getValue()); }
//*************
       /// System.out.println("LB before "); 
       // for(var mapping : leaderboard){ 
        	//System.out.println(mapping.getKey() + " ==> " + mapping.getValue()); }
//*************
        //this.leaderboard = sortedByValue;
        leaderboard = new HashMap<String, Integer>();
        for(Entry<String, Integer> mapping : entrySetSortedByValue){ 
        	leaderboard.put(mapping.getKey(), mapping.getValue());
        }
        	
        sortedLeaderboard = entrySetSortedByValue;
        
        for(Entry<String, Integer> mapping : sortedLeaderboard){ 
        	System.out.println(mapping.getKey() + " ==> " + mapping.getValue()); }
       }
	
	private Comparator<Entry<String, Integer>> valueComparator = new Comparator<Entry<String,Integer>>() { 
		@Override public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) { 
			System.out.println("Comparing");

			Integer v1 = e1.getValue(); 
			System.out.println(v1);
			Integer v2 = e2.getValue(); 
			System.out.println(v2);

			return v2.compareTo(v1); } 
		};

}