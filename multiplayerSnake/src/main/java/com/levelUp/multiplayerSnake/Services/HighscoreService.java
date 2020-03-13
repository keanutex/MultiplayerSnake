package com.levelUp.multiplayerSnake.Services;

import com.levelUp.multiplayerSnake.models.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class HighscoreService
{

    private ArrayList<Integer> Playerscores = new ArrayList<>(); // List for storing scores
    private int HighScore;

    public void createPlayer(Player player)
    {

    }

    public void savePlayerScore(int snakeLength, int numberOfPlayers)
    {
        Playerscores.add(snakeLength);
        if(numberOfPlayers ==0){
            Collections.sort(Playerscores, Collections.reverseOrder());
            checkHighScore(Playerscores.get(0));  
          }
    }
    private void checkHighScore(Integer curHighScore) {
        Scanner scanner;
        //int HighScore = 0;
        try {
            scanner = new Scanner(new File("doc/highscore.txt"));
            while(scanner.hasNextInt())
            {   HighScore = scanner.nextInt();}
        } catch (FileNotFoundException e) {
            e.printStackTrace();}
        if(HighScore<curHighScore)
        {updateHighscore(curHighScore);}

    }

    private void updateHighscore(Integer curHighScore) {

        try {
            FileWriter writer = new FileWriter("src/main/resources/static/highscore.txt", false);
            FileWriter writer1 = new FileWriter("doc/highscore.txt", false);
            writer.write(String.valueOf(curHighScore) );
            writer1.write(String.valueOf(curHighScore) );
            writer.close();
            writer1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        
    }
        //a function to return high score 

        public int getHighScore()
        {
            Scanner scanner;
            int HighScore = 0;
            try {
                scanner = new Scanner(new File("doc/highscore.txt"));
                while(scanner.hasNextInt())
                {   HighScore = scanner.nextInt();}
            } catch (FileNotFoundException e) {
                e.printStackTrace();}

            return HighScore;

        }
    }
 

    
