package com.levelUp.multiplayerSnake.models;

import java.util.ArrayList;

public class UpdatePayload {
    public ArrayList<Snake> snakes;
    public ArrayList<Pickup> pickups;
    public ArrayList<Bullet> bullets;
    public ArrayList<Wall> walls;
    //add any other values that need to be sent to the frontend in here

    public UpdatePayload(ArrayList<Snake> snakes, ArrayList<Pickup> pickups, ArrayList<Bullet> bullets, ArrayList<Wall> walls){
        this.snakes = snakes;
        this.pickups = pickups;
        this.bullets = bullets;
        this.walls = walls;
    }

    @Override
    public String toString() {
        return "UpdatePayload{" +
                "snakes=" + snakes +
                ", pickups=" + pickups +
                '}';
    }
}
