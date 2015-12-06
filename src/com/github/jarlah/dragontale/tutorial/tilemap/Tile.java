package com.github.jarlah.dragontale.tutorial.tilemap;

import java.awt.image.BufferedImage;

public class Tile {

	private BufferedImage image;
	
	private boolean blocking;

	public Tile(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public boolean isBlocking() {
		return blocking;
	}
	
	public void setBlocking(boolean b) {
		this.blocking = b;
	}

}
