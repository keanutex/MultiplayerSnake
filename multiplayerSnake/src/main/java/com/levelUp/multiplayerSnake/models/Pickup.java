package com.levelUp.multiplayerSnake.models;

public class Pickup {

    private int x;
    private int y;
    private String colour;
    private String type;

    public Pickup(int x, int y, String type){
        this.setX(x);
        this.setY(y);
        this.setType(type);
        switch (type) {
            case "food":
                this.setColour("#FFD700");
                break;
            case "speed":
                this.setColour("#000000");
                break;
            case "shoot":
                this.setColour("#949494");
                break;
            case "invincible":

                break;
        }
    }

    @Override
    public String toString() {
        return "Pickup{" +
                "x=" + getX() +
                ", y=" + getY() +
                '}';
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

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
