package com.levelUp.multiplayerSnake.models;

public class SnakeSegment {

  public int x;
  public int y;

  public SnakeSegment(int x, int y){
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "SnakeSegment{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }
}
