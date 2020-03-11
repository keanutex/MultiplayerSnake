package com.levelUp.multiplayerSnake.models;

public class Bullet {

    private int x;
    private int y;
    private String dir;
    final int increment = 10;
    private double speed;
    private double speedCounter =0;
    private double deathTimer = 0;

    public Bullet(int x, int y, String dir, double speed){
        this.setX(x);
        this.setY(y);
        this.setDir(dir);
        this.setSpeed(speed);
    }

    public void move(){
        switch(this.getDir()){
            case "up":
                this.setY(this.getY() - this.increment);
                break;
            case "down":
                this.setY(this.getY() + this.increment);
                break;
            case "right":
                this.setX(this.getX() +this.increment);
                break;
            case "left":
                this.setX(this.getX() - this.increment);
                break;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
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

    public double getDeathTimer() {
        return deathTimer;
    }

    public void setDeathTimer(double deathTimer) {
        this.deathTimer = deathTimer;
    }
}
