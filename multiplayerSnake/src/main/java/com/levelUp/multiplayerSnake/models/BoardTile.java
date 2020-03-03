package com.levelUp.multiplayerSnake.models;

public class BoardTile {
    private int x;
    private int y;
    private boolean snakeOccupied = false;
    private boolean pickupOccupied = false;
    private String pickup = "";

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

    public boolean isSnakeOccupied() {
        return snakeOccupied;
    }

    public void setSnakeOccupied(boolean snakeOccupied) {
        this.snakeOccupied = snakeOccupied;
    }

    public boolean isPickupOccupied() {
        return pickupOccupied;
    }

    public void setPickupOccupied(boolean pickupOccupied) {
        this.pickupOccupied = pickupOccupied;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }
}
