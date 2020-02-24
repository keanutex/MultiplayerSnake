package com.levelUp.multiplayerSnake;

import java.util.ArrayList;

public class Snake {
    private ArrayList<SnakeSegment> snakeSegments = new ArrayList<SnakeSegment>();


    public Snake(ArrayList<SnakeSegment> snakeSegments) {
        this.snakeSegments = snakeSegments;
    }


    public void changeDirection(String direction) {
        snakeSegments.get(0).dir = direction;

        int i = 0;
        for (SnakeSegment s :
                snakeSegments) {
            i++;
            snakeSegments.get(i).dir = snakeSegments.get(i - 1).dir;
        }
    }

    public void addSegment(String direction, int x, int y) {
        snakeSegments.add(new SnakeSegment(x, y, direction));
    }

}
