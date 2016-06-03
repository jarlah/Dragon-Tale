package com.github.jarlah.dragontale.tutorial.tilemap;

public enum Tile {
	EMPTY(0, -1, -1, false),
	GROUND(1, 0, 1, true),
	EARTH(2, 1, 1, true),
	STUFF(3, 2, 0, false);
	
	private final int id;
	private final int x;
	private final int y;
	private final boolean blocking;

	private Tile(int id, int x, int y, boolean blocking) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.blocking = blocking;
	}
	
	public static Tile valueOf(int id) {
		for (Tile t: values()){
			if (t.id == id) {
				return t;
			}
		}
		return null;
	}

	public boolean isBlocking() {
		return blocking;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
