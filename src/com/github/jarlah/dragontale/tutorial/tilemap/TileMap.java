package com.github.jarlah.dragontale.tutorial.tilemap;

public interface TileMap {

    int getTileSize();

    int getNumRows();

    int getNumCols();

    boolean isBlocking(int x, int y);

    double getXPoisition();

    double getYPosition();
}
