package com.github.jarlah.dragontale.tutorial.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.github.jarlah.dragontale.tutorial.main.GamePanel;

public class Background {
	private BufferedImage image;
	
	private double x, y, dx, dy, moveScale;
	
	public Background(String link, double ms) {
		try {
			image = ImageIO.read(Background.class.getClassLoader().getResourceAsStream(link));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		this.x += this.dx;
		this.y += this.dy;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, null);
		if (x < 0) {
			g.drawImage(image, (int) (x + GamePanel.WIDTH), (int) y, null);
		}
		if (y < 0) {
			g.drawImage(image, (int) x, (int) (y + GamePanel.HEIGHT), null);
		}
	}
}
