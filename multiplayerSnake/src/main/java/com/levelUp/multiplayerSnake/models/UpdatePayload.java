package com.levelUp.multiplayerSnake.models;

import java.util.ArrayList;

public class UpdatePayload {
    public ArrayList<Snake> snakes = new ArrayList<>();
    public ArrayList<Pickup> pickups = new ArrayList<>();

    public UpdatePayload(){

    }

    public UpdatePayload(ArrayList<Snake> snakes, ArrayList<Pickup> pickups){
        this.snakes = snakes;
        this.pickups = pickups;
    }

    @Override
    public String toString() {
        return "UpdatePayload{" +
                "snakes=" + snakes +
                ", pickups=" + pickups +
                '}';
    }
}
