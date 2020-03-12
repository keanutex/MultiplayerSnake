package com.levelUp.multiplayerSnake.models;

public class Message {
    private String name;
    private String message;
    private String colour;

    public Message(String name, String message, String colour) {
        this.name=name;
        this.message = message;
        this.colour = colour;
    }


    public String getColour() {
        return colour;
    }

    public String getName() {
        return name;
    }

    public String getMessage(){
        return  message;
    }
}
