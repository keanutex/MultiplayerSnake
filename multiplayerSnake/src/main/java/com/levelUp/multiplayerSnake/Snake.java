package com.levelUp.multiplayerSnake;

import java.util.ArrayList;

public class Snake {
    private ArrayList<SnakeSegment> snakeSegments = new ArrayList<SnakeSegment>();

    public Snake(){

    }

    public Snake(ArrayList<SnakeSegment> snakeSegments) {
        this.snakeSegments = snakeSegments;
    }

    public void createSnake(){
        snakeSegments.add(new SnakeSegment(3, 3, "up"));
        snakeSegments.add(new SnakeSegment(3, 4, "up"));
        snakeSegments.add(new SnakeSegment(3, 5, "up"));
    }

    public void move(){
        int x = 0;
        int y = 0;
        String direction = snakeSegments.get(0).dir;
        switch(snakeSegments.get(0).dir){
            case "up":
                y  =snakeSegments.get(0).y + 1;
                break;
            case "down":
                y  =snakeSegments.get(0).y - 1;
                break;
            case "right":
                x  =snakeSegments.get(0).x + 1;
                break;
            case "left":
                x  =snakeSegments.get(0).x - 1;
                break;
        }
        snakeSegments.add(0, new SnakeSegment(x, y, direction ));
        snakeSegments.remove(snakeSegments.size() - 1);
    }

    public void changeDirection(String direction) {
        snakeSegments.get(0).dir = direction;
    }

    public void addSegment(String direction, int x, int y) {
        snakeSegments.add(new SnakeSegment(x, y, direction));
    }

}
