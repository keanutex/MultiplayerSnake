package com.levelUp.multiplayerSnake.models;

import java.util.ArrayList;

public class UpdatePayload {
    public ArrayList<Snake> snakes;
    public ArrayList<Pickup> pickups;
    //add any other values that need to be sent to the frontend in here

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
