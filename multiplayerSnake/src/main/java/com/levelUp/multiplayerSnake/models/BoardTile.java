package com.levelUp.multiplayerSnake.models;

public class BoardTile {
    private int x;
    private int y;
    private boolean snakeOccupied = false;
    private Pickup pickupOnTile = null;

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

    public Pickup getPickupOnTile() {
        return pickupOnTile;
    }

    public void setPickupOnTile(Pickup pickupOnTile) {
        this.pickupOnTile = pickupOnTile;
    }
}
