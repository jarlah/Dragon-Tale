package com.github.jarlah.dragontale.tutorial.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.github.jarlah.dragontale.tutorial.main.GamePanel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tiled.core.Map;
import tiled.io.TMXMapReader;

public class PlainFileTileMap implements TileMap {

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

    // tileset
    private BufferedImage tileset;
    private int numTilesHorizontal;
    private int numTilesVertical;
    private BufferedImage[][] tiles;

    // drawing
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    public PlainFileTileMap(int tileSize) {
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tween = 0.07;
    }

    public void loadTiles(String s) {
        try {
            tileset = ImageIO.read(getClass().getClassLoader().getResourceAsStream(s));
            numTilesHorizontal = tileset.getWidth() / tileSize;
            numTilesVertical = tileset.getHeight() / tileSize;
            tiles = new BufferedImage[numTilesVertical][numTilesHorizontal];
            for (int col = 0; col < numTilesHorizontal; col++) {
                for (int row = 0; row < numTilesVertical; row++) {
                    int spriteX = col * tileSize;
                    int spriteY = row == 0 ? 0 : tileSize;
                    tiles[row][col] = tileset.getSubimage(spriteX, spriteY, tileSize, tileSize);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTmx(String tmxLocation) {
        TMXMapReader reader = new TMXMapReader();
        try {
            Map tmxMap = reader.readMap(getClass().getClassLoader().getResourceAsStream(tmxLocation));

        } catch (Exception ex) {
            Logger.getLogger(PlainFileTileMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadMap(String s) {
        try {

            String delims = "\\s+";
            InputStream in = getClass().getClassLoader().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            int cols = Integer.parseInt(br.readLine());
            int rows = Integer.parseInt(br.readLine());
            map = new Tile[rows][cols];
            for (int row = 0; row < rows && br.ready(); row++) {
                String line = br.readLine();
                String[] columns = line.split(delims);
                for (int col = 0; col < columns.length; col++) {
                    Tile type = Tile.valueOf(Integer.parseInt(columns[col]));
                    map[row][col] = type;
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new IllegalStateException("Could not load plain map", e);
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

    @Override
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

                Tile info = map[row][col];

                g.drawImage(tiles[info.getY()][info.getX()], (int) x + col * tileSize, (int) y + row * tileSize, null);

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
