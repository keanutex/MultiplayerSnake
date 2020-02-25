package com.levelUp.multiplayerSnake.models;

import java.util.ArrayList;
import java.util.UUID;

public class Snake {

    private ArrayList<SnakeSegment> snakeSegments = new ArrayList<SnakeSegment>();

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
    }

    public void createSnake(){
        this.snakeSegments.add(new SnakeSegment(3, 3, "up"));
        this.snakeSegments.add(new SnakeSegment(3, 4, "up"));
        this.snakeSegments.add(new SnakeSegment(3, 5, "up"));
    }

    public void move(){
        int x = 0;
        int y = 0;
        String direction = this.snakeSegments.get(0).dir;
        switch(this.snakeSegments.get(0).dir){
            case "up":
                y  = this.snakeSegments.get(0).y + 1;
                break;
            case "down":
                y  = this.snakeSegments.get(0).y - 1;
                break;
            case "right":
                x  = this.snakeSegments.get(0).x + 1;
                break;
            case "left":
                x  = this.snakeSegments.get(0).x - 1;
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
