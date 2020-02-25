package com.levelUp.multiplayerSnake.models;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Snake {

    public ArrayList<SnakeSegment> snakeSegments = new ArrayList<SnakeSegment>();

    public boolean gameRunning = false;
    public boolean playerAlive = true;
    public boolean bestPlayer = false;
    public int playerNr;
    public int score;
    public long playTime;

    public String playerColor;
    public String playerName;
    public UUID client;

    public Snake(){
        int n = new Random().nextInt(100);
        this.snakeSegments.add(new SnakeSegment(n, n, "up"));
        this.snakeSegments.add(new SnakeSegment(n +10, n +10, "up"));
        this.snakeSegments.add(new SnakeSegment(n +20, n +20, "up"));
    }

    public void move(){
        int increment = 10;
        int x = 0;
        int y = 0;
        String direction = this.snakeSegments.get(0).dir;
        switch(this.snakeSegments.get(0).dir){
            case "up":
                y  = this.snakeSegments.get(0).y + increment;
                x = this.snakeSegments.get(0).x;
                break;
            case "down":
                y  = this.snakeSegments.get(0).y - increment;
                x = this.snakeSegments.get(0).x;
                break;
            case "right":
                x  = this.snakeSegments.get(0).x + increment;
                y  = this.snakeSegments.get(0).y;
                break;
            case "left":
                x  = this.snakeSegments.get(0).x - increment;
                y  = this.snakeSegments.get(0).y;
                break;
        }
        this.snakeSegments.add(0, new SnakeSegment(x, y, direction ));
        this.snakeSegments.remove(this.snakeSegments.size() - 1);
    }

    public void changeDirection(String direction) {
        this.snakeSegments.get(0).dir = direction;
    }

    public void addSegment(String direction, int x, int y) {
        this.snakeSegments.add(new SnakeSegment(x, y, direction));
    }


}
