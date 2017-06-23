package com.github.jarlah.dragontale.tutorial.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Explosion {

    private int width, height, x, y, ymap, xmap;
    private final Animation animation;
    private final BufferedImage[] sprites;
    private boolean remove = false;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
        this.height = 64;
        this.width = 65;

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Sprites/Enemies/explosion.png"));
            sprites = new BufferedImage[5];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = spriteSheet.getSubimage(
                        i * width,
                        0,
                        width,
                        height
                );
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not load explosion", e);
        }

        animation = new Animation();
        animation.setFrames(sprites, width, height);
        animation.setDelay(70);
    }

    public boolean shouldRemove() {
        return remove;
    }

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
    }

    public void update() {
        animation.update();
        if (animation.hasPlayedOnce()) {
            remove = true;
        }
    }
}
