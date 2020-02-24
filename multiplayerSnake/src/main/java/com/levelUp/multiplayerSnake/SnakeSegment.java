package com.levelUp.multiplayerSnake;

public class SnakeSegment {

  public SnakeSegment(){

  }

  public SnakeSegment(int x, int y, String dir){
    this.x = x;
    this.y = y;
    this.dir = dir;
  }

  public int x;
  public int y;
  public String dir;


}
