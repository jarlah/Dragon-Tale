package com.github.jarlah.dragontale.tutorial.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.github.jarlah.dragontale.tutorial.tilemap.TileMap;
import java.io.IOException;

public class FireBall extends Actor {

    private boolean hit, remove;
    private final BufferedImage[] sprites;
    private final BufferedImage[] hitSprites;

    public FireBall(TileMap tm, boolean right) {
        super(tm);

        facingRight = right;

        moveSpeed = 3.0;
        if (right) {
            dx = moveSpeed;
        } else {
            dx = -moveSpeed;
        }

        width = 30;
        height = 30;
        cwidth = 14;
        cheight = 14;

        try {
            BufferedImage spritesheet = ImageIO.read(getClass()
                    .getClassLoader().getResourceAsStream(
                            "Sprites/Player/fireball.gif"));
            sprites = new BufferedImage[4];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
            }
            hitSprites = new BufferedImage[3];
            for (int i = 0; i < hitSprites.length; i++) {
                hitSprites[i] = spritesheet.getSubimage(i * width, height, width, height);
            }

            animation = new Animation();
            animation.setFrames(sprites, width, height);
            animation.setDelay(70);

        } catch (IOException e) {
            throw new IllegalStateException("Could not load fireball", e);
        }
    }

    public void setHit() {
        if (hit) {
            return;
        }
        hit = true;
        animation.setFrames(hitSprites, width, height);
        animation.setDelay(70);
        dx = 0;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public void update() {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        if (dx == 0 && !hit) {
            setHit();
        }

        animation.update();

        if (hit && animation.hasPlayedOnce()) {
            remove = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        setMapPosition();

        super.draw(g);
    }

}
