package com.levelUp.multiplayerSnake.models;

import java.util.ArrayList;
import java.util.Random;

public class Snake {

    public ArrayList<SnakeSegment> snakeSegments = new ArrayList<SnakeSegment>();
    public String playerColour;
    public double speed;
    public double speedCounter = 0;
    public boolean directionChanged = false;
    private String name;

    public Snake(double speed){
        this.speed = speed;
        placeSnake();
    }

    public Snake(double speed,String Colour,String name){
        this.speed = speed;
        this.playerColour = Colour;
        this.name = name;
        placeSnake();
    }


    private void placeSnake(){
        int n = new Random().nextInt(1000);
        n = (int) (Math.round(n/10.0) * 10);
        this.snakeSegments.add(new SnakeSegment(n, n, "up"));
        this.snakeSegments.add(new SnakeSegment(n +10, n +10, "up"));
        this.snakeSegments.add(new SnakeSegment(n +20, n +20, "up"));
        this.snakeSegments.add(new SnakeSegment(n +30, n +30, "up"));
        this.snakeSegments.add(new SnakeSegment(n +40, n +40, "up"));
    }

    public void move(){
        int increment = 10;
        int x = 0;
        int y = 0;
        String direction = this.snakeSegments.get(0).dir;
        switch(this.snakeSegments.get(0).dir){
            case "up":
                y  = this.snakeSegments.get(0).y - increment;
                x = this.snakeSegments.get(0).x;
                break;
            case "down":
                y  = this.snakeSegments.get(0).y + increment;
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

    public void addSegment() {
        this.snakeSegments.add(new SnakeSegment(this.snakeSegments.get(this.snakeSegments.size() - 1).x, this.snakeSegments.get(this.snakeSegments.size() - 1).y, ""));
    }

    public String getDirection(){
        return this.snakeSegments.get(0).dir;
    }

    public String getName(){return name;}


    @Override
    public String toString() {
        return "Snake{" +
                "snakeSegments=" + snakeSegments +
                ", playerColour='" + playerColour + '\'' +
                '}';
    }
}
