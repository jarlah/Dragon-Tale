package com.github.jarlah.dragontale.tutorial.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Explosion {
	private int width, height, x, y, ymap, xmap;
	private Animation animation;
	private BufferedImage[] sprites;
	private boolean remove = false;
	
	public Explosion(int x, int y) {
		this.x = x;
		this.y = y;
		this.height = 64;
		this.width = 65;
		
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Sprites/Enemies/explosion2.png"));
			sprites = new BufferedImage[5];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(
					i * width, 
					0, 
					width, 
					height
				);
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		animation = new Animation();
		animation.setFrames(sprites, width, height);
		animation.setDelay(70);
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void setMapPosition(int x, int y) {
		xmap = x;
		ymap = y;
	}

	public void draw(Graphics2D g) {
		g.drawImage(
			animation.getImage(),
			x + xmap - width / 2,
			y + ymap - height / 2,
			null
		);
		System.out.println("Drawed");
	}
	
	public void update() {
		animation.update();
		if (animation.hasPlayedOnce()) {
			System.out.println("Dead");
			remove = true;
		}
		System.out.println("Updated");
	}
}
