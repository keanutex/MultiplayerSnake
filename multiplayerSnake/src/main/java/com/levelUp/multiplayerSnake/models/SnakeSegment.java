package com.levelUp.multiplayerSnake.models;

public class SnakeSegment {

  public SnakeSegment(){

  }

  public int x;
  public int y;
  public String dir;

  public SnakeSegment(int x, int y, String dir){
    this.x = x;
    this.y = y;
    this.dir = dir;
  }

}
