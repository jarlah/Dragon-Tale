package com.github.jarlah.dragontale.tutorial.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.github.jarlah.dragontale.tutorial.main.GamePanel;

public class TileMap {

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
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;

	// tileset
	private BufferedImage tileset;
	private int numTilesHorizontal;
	private int numTilesVertical;
	private Tile[][] tiles;

	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	private TileFactory tileFactory;

	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
		tileFactory = new TileFactory() {
			public Tile createTile(BufferedImage _tileSet, int _row, int _col, int _tileSize) {
				return new Tile(_tileSet.getSubimage(
					_col * _tileSize,
					_row == 0 ? 0 : _tileSize, 
					_tileSize, _tileSize
				), _row == 0 ? Tile.NORMAL : Tile.BLOCKED);
			}
		};
	}

	interface TileFactory {
		public Tile createTile(BufferedImage _tileSet, int _row, int _col,
				int _tileSize);
	}

	public void loadTiles(String s) {
		try {
			tileset = ImageIO.read(getClass().getClassLoader()
					.getResourceAsStream(s));
			numTilesHorizontal = tileset.getWidth() / tileSize;
			numTilesVertical = tileset.getHeight() / tileSize;
			tiles = new Tile[numTilesVertical][numTilesHorizontal];
			for (int col = 0; col < numTilesHorizontal; col++) {
				for (int row = 0; row < numTilesVertical; row++) {
					tiles[row][col] = tileFactory.createTile(tileset, row, col,
							tileSize);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadMap(String s) {

		try {

			InputStream in = getClass().getClassLoader().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;

			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;

			String delims = "\\s+";
			for (int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for (int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getTileSize() {
		return tileSize;
	}

	public int getx() {
		return (int) x;
	}

	public int gety() {
		return (int) y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesHorizontal;
		int c = rc % numTilesHorizontal;
		return tiles[r][c].getType();
	}

	public void setTween(double d) {
		tween = d;
	}

	public void setPosition(double x, double y) {

		System.out.println(this.x);
		System.out.println((x - this.x) * tween);

		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;

		System.out.println(this.x + "\n==========");

		fixBounds();

		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;

	}

	private void fixBounds() {
		if (x < xmin)
			x = xmin;
		if (y < ymin)
			y = ymin;
		if (x > xmax)
			x = xmax;
		if (y > ymax)
			y = ymax;
	}

	public void draw(Graphics2D g) {

		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {

			if (row >= numRows)
				break;

			for (int col = colOffset; col < colOffset + numColsToDraw; col++) {

				if (col >= numCols)
					break;

				if (map[row][col] == 0)
					continue;

				int rc = map[row][col];
				int r = rc / numTilesHorizontal;
				int c = rc % numTilesHorizontal;

				g.drawImage(tiles[r][c].getImage(), (int) x + col * tileSize,
						(int) y + row * tileSize, null);

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
