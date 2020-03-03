package com.levelUp.multiplayerSnake.models;

public class Board {
    public BoardTile[][] instance = new BoardTile[100][100];

    public Board(){
        for(int i = 0; i < instance.length; i ++){
            for(int j  = 0; j < instance.length; j++){
                instance[i][j] = new BoardTile();
            }
        }
    }
}
