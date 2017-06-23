package com.github.jarlah.dragontale.tutorial.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.github.jarlah.dragontale.tutorial.main.GamePanel;
import java.io.IOException;

public class Background {

    private BufferedImage image;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private double moveScale;

    public Background(String s, double ms) {

        try {
            image = ImageIO.read(getClass().getClassLoader()
                    .getResourceAsStream(s));
            moveScale = ms;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load background", e);
        }

    }

    public void setPosition(double x, double y) {
        this.x = (x * moveScale) % GamePanel.GAME_WIDTH;
        this.y = (y * moveScale) % GamePanel.GAME_HEIGHT;
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics2D g) {

        g.drawImage(image, (int) x, (int) y, null);

        if (x < 0) {
            g.drawImage(image, (int) x + GamePanel.GAME_WIDTH, (int) y, null);
        }
        if (x > 0) {
            g.drawImage(image, (int) x - GamePanel.GAME_WIDTH, (int) y, null);
        }
    }

}
