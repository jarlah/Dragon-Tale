package com.github.jarlah.dragontale.tutorial.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.github.jarlah.dragontale.tutorial.main.GamePanel;
import java.util.logging.Level;
import java.util.logging.Logger;
import tiled.core.Map;
import tiled.core.TileLayer;
import tiled.io.TMXMapReader;

public class TmxFileTileMap implements TileMap {

    // position
    private double x;
    private double y;

    // bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;

    private double tween;

    // map
    private Tile[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;

    // drawing
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    private Map tmxMap;

    public TmxFileTileMap(int tileSize) {
        tween = 0.07;
    }

    public void loadMap(String tmxLocation) {        
        try {
            tmxMap = new TMXMapReader().readMap(tmxLocation);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not load tmx map", ex);
        }
        
        tileSize = tmxMap.getTileHeight();
        numRowsToDraw = tmxMap.getHeight();
        numColsToDraw = tmxMap.getWidth();
        TileLayer layer = (TileLayer) tmxMap.getLayers().get(0);
        int cols = layer.getWidth();
        int rows = layer.getHeight();
        map = new Tile[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (((TileLayer) tmxMap.getLayer(0)).getTileAt(col, row) != null) {
                    map[row][col] = Tile.GROUND;
                } else {
                    map[row][col] = Tile.EMPTY;
                }
            }
        }

        numRows = map.length;
        numCols = map[0].length;

        width = numCols * tileSize;
        height = numRows * tileSize;

        xmin = GamePanel.WIDTH - width;
        xmax = 0;
        ymin = GamePanel.HEIGHT - height;
        ymax = 0;
    }

    @Override
    public int getTileSize() {
        return tileSize;
    }

    @Override
    public double getXPoisition() {
        return x;
    }

    @Override
    public double getYPosition() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isBlocking(int row, int col) {
        return map[row][col].isBlocking();
    }

    public void setTween(double d) {
        tween = d;
    }

    public void setPosition(double x, double y) {

        //System.out.println(this.x);
        //System.out.println((x - this.x) * tween);
        this.x += (x - this.x) * tween;
        this.y += (y - this.y) * tween;

        //System.out.println(this.x + "\n==========");
        fixBounds();

        colOffset = (int) -this.x / tileSize;
        rowOffset = (int) -this.y / tileSize;

    }

    private void fixBounds() {
        if (x < xmin) {
            x = xmin;
        }
        if (y < ymin) {
            y = ymin;
        }
        if (x > xmax) {
            x = xmax;
        }
        if (y > ymax) {
            y = ymax;
        }
    }

    public void draw(Graphics2D g) {
        TileLayer layer = (TileLayer) tmxMap.getLayers().get(0);

        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {

            if (row >= numRows) {
                break;
            }

            for (int col = colOffset; col < colOffset + numColsToDraw; col++) {

                if (col >= numCols) {
                    break;
                }

                if (Tile.EMPTY == map[row][col]) {
                    continue;
                }

                tiled.core.Tile tile = layer.getTileAt(col, row);
                if (tile == null) {
                    continue;
                }
                final int width = (int) x + col * tmxMap.getTileWidth();
                final int height = (int) y + row * tmxMap.getTileHeight();

                g.drawImage(tile.getImage(), width, height,
                        null
                );

            }

        }

    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

}
