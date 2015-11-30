package com.github.jarlah.dragontale.tutorial.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.github.jarlah.dragontale.tutorial.entity.Animation;
import com.github.jarlah.dragontale.tutorial.entity.Enemy;
import com.github.jarlah.dragontale.tutorial.tilemap.TileMap;

public class Bahamut extends Enemy {
	private BufferedImage[] sprites;
	
	public Bahamut(TileMap tm) {
		super(tm);
		
		moveSpeed = maxSpeed = 0.2;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		health = maxHealth = 100;
		damage = 1;
		
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Sprites/Enemies/bahamut.png"));
			width = cwidth = spriteSheet.getWidth() / 4;
			height = cheight = spriteSheet.getHeight() / 4;
			cheight -= 35;
			cwidth -= 45;
			sprites = new BufferedImage[4];
			sprites[0] = spriteSheet.getSubimage(0, 2 * height, width, height);
			sprites[1] = spriteSheet.getSubimage(width, 2 * height, width, height);
			sprites[2] = spriteSheet.getSubimage(2 * width, 2 * height, width, height);
			sprites[3] = spriteSheet.getSubimage(3 * width, 2 * height, width, height);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites, width, height);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
	}


	public void getNextPosition() {
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		if (falling) {
			dy += fallSpeed;
		}
	}

	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 400) {
				flinching = false;
			}
		}
		
		if (right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		} else if (left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		//	if (notOnScreen()) return;
		
		setMapPosition();
		
		super.draw(g);
	}
}
