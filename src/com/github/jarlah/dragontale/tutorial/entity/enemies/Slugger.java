package com.github.jarlah.dragontale.tutorial.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.github.jarlah.dragontale.tutorial.entity.Animation;
import com.github.jarlah.dragontale.tutorial.entity.Enemy;
import com.github.jarlah.dragontale.tutorial.tilemap.TileMap;
import java.io.IOException;

public class Slugger extends Enemy {

    private final BufferedImage[] sprites;

    public Slugger(TileMap tm) {
        super(tm);

        moveSpeed = maxSpeed = 0.3;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = height = 30;
        cwidth = cheight = 20;

        health = maxHealth = 2;
        damage = 1;

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Sprites/Enemies/slugger.gif"));
            sprites = new BufferedImage[3];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not load slugger", e);
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
        super.update();

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

        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 100 % 2 == 0) {
                return;
            }
        }

        super.draw(g);
    }
}
