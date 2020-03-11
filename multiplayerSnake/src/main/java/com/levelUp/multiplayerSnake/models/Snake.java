package com.levelUp.multiplayerSnake.models;

import java.util.ArrayList;
import java.util.Random;

public class Snake {

    private ArrayList<SnakeSegment> snakeSegments = new ArrayList<SnakeSegment>();
    private String dir;
     private String playerColour;
     private double speed;
     private double speedCounter = 100;
     private boolean directionChanged = false;
     private boolean snakeMoved = false;
    private int boostSpeedCounter = 0;
     private double baseSpeed = 0;
     private boolean speedBoost = false;
     private boolean canShoot = false;
     private double shootCounter = 0;


    public Snake(double baseSpeed, String dir){
        this.setDir(dir);
        this.setSpeed(baseSpeed);
        this.setBaseSpeed(baseSpeed);
        int n = generateRandomCoOrd(100, 900);
        this.getSnakeSegments().add(new SnakeSegment(n, n));
        this.getSnakeSegments().add(new SnakeSegment(n +10, n +10));
        this.getSnakeSegments().add(new SnakeSegment(n +20, n +20));
        this.getSnakeSegments().add(new SnakeSegment(n +30, n +30));
        this.getSnakeSegments().add(new SnakeSegment(n +40, n +40));
    }
    public void removeTail(){
        getSnakeSegments().remove(tail());
    }

    public SnakeSegment head(){
        return getSnakeSegments().get(0);
    }
    public SnakeSegment tail(){
        return getSnakeSegments().get(getSnakeSegments().size() - 1);
    }

    public void move(){
        int x = 0;
        int y = 0;
        switch(this.getDir()){
            case "up":
                y  = this.getSnakeSegments().get(0).getY() - this.getIncrement();
                x = this.getSnakeSegments().get(0).getX();
                break;
            case "down":
                y  = this.getSnakeSegments().get(0).getY() + this.getIncrement();
                x = this.getSnakeSegments().get(0).getX();
                break;
            case "right":
                x  = this.getSnakeSegments().get(0).getX() + this.getIncrement();
                y  = this.getSnakeSegments().get(0).getY();
                break;
            case "left":
                x  = this.getSnakeSegments().get(0).getX() - this.getIncrement();
                y  = this.getSnakeSegments().get(0).getY();
                break;
        }
        this.getSnakeSegments().add(0, new SnakeSegment(x, y));
        this.getSnakeSegments().remove(this.getSnakeSegments().size() - 1);
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
        this.setDir(direction);
    }

    public void addSegment() {
        this.getSnakeSegments().add(new SnakeSegment(this.getSnakeSegments().get(this.getSnakeSegments().size() - 1).getX(), this.getSnakeSegments().get(this.getSnakeSegments().size() - 1).getY()));
    }

    public String getDirection(){
        return this.getDir();
    }
    
    public int getLength() {
    	return this.getSnakeSegments().size();
    }




    public ArrayList<SnakeSegment> getSnakeSegments() {
        return snakeSegments;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getPlayerColour() {
        return playerColour;
    }

    public void setPlayerColour(String playerColour) {
        this.playerColour = playerColour;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeedCounter() {
        return speedCounter;
    }

    public void setSpeedCounter(double speedCounter) {
        this.speedCounter = speedCounter;
    }

    public boolean isDirectionChanged() {
        return directionChanged;
    }

    public void setDirectionChanged(boolean directionChanged) {
        this.directionChanged = directionChanged;
    }

    public boolean isSnakeMoved() {
        return snakeMoved;
    }

    public void setSnakeMoved(boolean snakeMoved) {
        this.snakeMoved = snakeMoved;
    }

    public int getIncrement() {
        int increment = 10;
        return increment;
    }

    public int getBoostSpeedCounter() {
        return boostSpeedCounter;
    }

    public void setBoostSpeedCounter(int boostSpeedCounter) {
        this.boostSpeedCounter = boostSpeedCounter;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public boolean isSpeedBoost() {
        return speedBoost;
    }

    public void setSpeedBoost(boolean speedBoost) {
        this.speedBoost = speedBoost;
    }

    public boolean isCanShoot() {
        return canShoot;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public double getShootCounter() {
        return shootCounter;
    }

    public void setShootCounter(double shootCounter) {
        this.shootCounter = shootCounter;
    }

    @Override
    public String toString() {
        return "Snake{" +
                "snakeSegments=" + getSnakeSegments() +
                ", playerColour='" + getPlayerColour() + '\'' +
                '}';
    }
}
