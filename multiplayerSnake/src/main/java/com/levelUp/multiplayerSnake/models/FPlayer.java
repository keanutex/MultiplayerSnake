package com.levelUp.multiplayerSnake.models;

import java.util.UUID;

public class FPlayer {

    private UUID clientId;
    private String color;
    private FPosition FPosition;
    private int score;

    public FPlayer(UUID clientId, String color, FPosition FPosition, int score) {
        this.clientId = clientId;
        this.color = color;
        this.FPosition = FPosition;
        this.score = score;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public FPosition getFPosition() {
        return FPosition;
    }

    public void setFPosition(FPosition FPosition) {
        this.FPosition = FPosition;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

