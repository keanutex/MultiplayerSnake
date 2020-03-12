package com.levelUp.multiplayerSnake.models;

public class LoggingMessage {
    private String message;
    private String colour;


    public LoggingMessage(String message, String colour){
        this.message = message;
        this.colour = colour;
    }

    public String getMessage() {
        return message;
    }

    public String getColour() {
        return colour;
    }
}
