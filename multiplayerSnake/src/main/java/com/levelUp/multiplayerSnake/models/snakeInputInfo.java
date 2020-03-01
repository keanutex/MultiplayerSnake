package com.levelUp.multiplayerSnake.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class snakeInputInfo {
    private String colour;
    private String name;

    @JsonCreator
    public snakeInputInfo(@JsonProperty("colour") String colour, @JsonProperty("name") String name )
    {
        this.colour = colour;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String getColour(){
        return this.colour;
    }

    @Override
    public String toString() {

        return "{colour: " + colour + ", name:" + name +" }";


    }
}
