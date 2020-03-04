package com.levelUp.multiplayerSnake.models;

public class Pickup {

    public int x;
    public int y;
    public String colour;
    public String type;

    public Pickup(int x, int y, String type){
        this.x = x;
        this.y = y;
        this.type = type;
        if(type.equals("food")){
            this.colour = "#FFD700";
        }else if(type.equals("speed")){
            this.colour = "#000000";
        }else if(type.equals("shoot")){
            this.colour = "#949494";
        }else if(type.equals("invincible")){

        }
    }

    @Override
    public String toString() {
        return "Pickup{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
