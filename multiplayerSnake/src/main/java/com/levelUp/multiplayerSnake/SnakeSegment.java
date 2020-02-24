package com.levelUp.multiplayerSnake;

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


  public void move(){
    switch (this.dir){
      case "up":
        this.y += 1;
        break;
      case "down":
        this.y -= 1;
        break;
      case "right":
        this.x += 1;
        break;
      case "left":
        this.x -= 1;
        break;
    }
  }

}
