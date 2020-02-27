package com.levelUp.multiplayerSnake.models;

public class Pickup {

    public int x;
    public int y;

    public Pickup(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Pickup{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
