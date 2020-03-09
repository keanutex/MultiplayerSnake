package com.levelUp.multiplayerSnake.models;

import java.util.ArrayList;
import java.util.Random;

public class Snake {

    public ArrayList<SnakeSegment> snakeSegments = new ArrayList<SnakeSegment>();
    String dir;
    public String playerColour;
    public double speed;
    public double speedCounter = 100;
    public boolean directionChanged = false;
    public boolean snakeMoved = false;
    final int increment = 10;
    public int boostSpeedCounter = 0;
    public double baseSpeed = 0;
    public boolean speedBoost = false;
    public boolean canShoot = false;
    public double shootCounter = 0;


    public Snake(double baseSpeed, String dir){
        this.dir = dir;
        this.speed = baseSpeed;
        this.baseSpeed = baseSpeed;
        int n = generateRandomCoOrd(100, 900);
        this.snakeSegments.add(new SnakeSegment(n, n));
        this.snakeSegments.add(new SnakeSegment(n +10, n +10));
        this.snakeSegments.add(new SnakeSegment(n +20, n +20));
        this.snakeSegments.add(new SnakeSegment(n +30, n +30));
        this.snakeSegments.add(new SnakeSegment(n +40, n +40));
    }
    public void removeTail(){
        snakeSegments.remove(tail());
    }

    public SnakeSegment head(){
        return snakeSegments.get(0);
    }
    public SnakeSegment tail(){
        return snakeSegments.get(snakeSegments.size() - 1);
    }

    public void move(){
        int x = 0;
        int y = 0;
        switch(this.dir){
            case "up":
                y  = this.snakeSegments.get(0).y - this.increment;
                x = this.snakeSegments.get(0).x;
                break;
            case "down":
                y  = this.snakeSegments.get(0).y + this.increment;
                x = this.snakeSegments.get(0).x;
                break;
            case "right":
                x  = this.snakeSegments.get(0).x + this.increment;
                y  = this.snakeSegments.get(0).y;
                break;
            case "left":
                x  = this.snakeSegments.get(0).x - this.increment;
                y  = this.snakeSegments.get(0).y;
                break;
        }
        this.snakeSegments.add(0, new SnakeSegment(x, y));
        this.snakeSegments.remove(this.snakeSegments.size() - 1);
    }

    private int generateRandomCoOrd(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min;
        number = (int) (Math.round(number / 10.0) * 10);
        return number;
    }


    public void changeDirection(String direction) {
        this.dir = direction;
    }

    public void addSegment() {
        this.snakeSegments.add(new SnakeSegment(this.snakeSegments.get(this.snakeSegments.size() - 1).x, this.snakeSegments.get(this.snakeSegments.size() - 1).y));
    }

    public String getDirection(){
        return this.dir;
    }
    
    public int getLength() {
    	return this.snakeSegments.size(); 	
    }


    @Override
    public String toString() {
        return "Snake{" +
                "snakeSegments=" + snakeSegments +
                ", playerColour='" + playerColour + '\'' +
                '}';
    }
}
