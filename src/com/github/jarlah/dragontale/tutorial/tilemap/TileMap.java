package com.github.jarlah.dragontale.tutorial.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
	private TileInfo[][] map;
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
				));
			}
		};
	}

	interface TileFactory {
		public Tile createTile(BufferedImage _tileSet, int _row, int _col, int _tileSize);
	}

	public void loadTiles(String s) {
		try {
			tileset = ImageIO.read(getClass().getClassLoader().getResourceAsStream(s));
			numTilesHorizontal = tileset.getWidth() / tileSize;
			numTilesVertical = tileset.getHeight() / tileSize;
			tiles = new Tile[numTilesVertical][numTilesHorizontal];
			for (int col = 0; col < numTilesHorizontal; col++) {
				for (int row = 0; row < numTilesVertical; row++) {
					tiles[row][col] = tileFactory.createTile(tileset, row, col, tileSize);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class TileInfo { 
		boolean blocking; 
		int col; 
		int row;
		
		public TileInfo(boolean b, int col, int row) { 
			this.blocking = b; 
			this.col = col;
			this.row = row;
		}
	}

	class EmptyTile extends TileInfo{
		public EmptyTile() {
			super(false, -1, -1);
		}
	}
	
	public void loadMap(String s) {

		try {

			InputStream in = getClass().getClassLoader().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String delims = "\\s+";
			List<List<TileInfo>> tileInfo = new ArrayList<>();
			while(br.ready()) {
				String row = br.readLine();
				String[] columns = row.split(delims);
				List<TileInfo> tileList = new ArrayList<>();
				for (int col = 0; col < columns.length; col ++) {
					String column = columns[col];
					if (column.equals("0")) {
						tileList.add(new EmptyTile());
						continue;
					}
					String[] spec = column.split(";");
					boolean blocking = false;
					int colPos, rowPos;
					if(spec.length == 3) {
						blocking = spec[0].equals("b");
						colPos = Integer.parseInt(spec[1]);
						rowPos = Integer.parseInt(spec[2]);
					} else if (spec.length == 2){
						colPos = Integer.parseInt(spec[0]);
						rowPos = Integer.parseInt(spec[1]);
					} else {
						throw new IllegalArgumentException("Wrong column info " + column);
					}
					TileInfo info = new TileInfo(blocking, colPos, rowPos);
					tileList.add(info);
				}
				tileInfo.add(tileList);
			}
			
			final int listSize = tileInfo.size();
			map = new TileInfo[listSize][];
			for(int i = 0; i < listSize; i++) {
			    List<TileInfo> sublist = tileInfo.get(i);
			    final int sublistSize = sublist.size();
			    map[i] = new TileInfo[sublistSize];
			    for(int j = 0; j < sublistSize; j++) {
			    	map[i][j] = sublist.get(j);
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

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getTileSize() {
		return tileSize;
	}

	public double getx() {
		return x;
	}

	public double gety() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isBlocking(int row, int col) {
		TileInfo info = map[row][col];
		if (info instanceof EmptyTile) return false;
		return info.blocking;
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

				if (map[row][col] instanceof EmptyTile)
					continue;

				TileInfo info = map[row][col];

				g.drawImage(tiles[info.row][info.col].getImage(), (int) x + col * tileSize,
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
