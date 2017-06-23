package com.github.jarlah.dragontale.tutorial.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.github.jarlah.dragontale.tutorial.entity.Animation;
import com.github.jarlah.dragontale.tutorial.entity.Enemy;
import com.github.jarlah.dragontale.tutorial.tilemap.TileMap;

public class RedBird extends Enemy {

    private BufferedImage[] sprites;

    public RedBird(TileMap tm) {
        super(tm);

        moveSpeed = maxSpeed = 1.0;
        fallSpeed = 0.0;
        maxFallSpeed = 0.0;

        health = maxHealth = 2;
        damage = 1;

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Sprites/Enemies/redbird.png"));
            width = cwidth = spriteSheet.getWidth() / 5;
            height = cheight = spriteSheet.getHeight() / 3;
            sprites = new BufferedImage[14];
            sprites[0] = spriteSheet.getSubimage(0, 0, width, height);
            sprites[1] = spriteSheet.getSubimage(30, 0, width, height);
            sprites[2] = spriteSheet.getSubimage(60, 0, width, height);
            sprites[3] = spriteSheet.getSubimage(90, 0, width, height);
            sprites[4] = spriteSheet.getSubimage(120, 0, width, height);
            sprites[5] = spriteSheet.getSubimage(0, 27, width, height);
            sprites[6] = spriteSheet.getSubimage(30, 27, width, height);
            sprites[7] = spriteSheet.getSubimage(60, 27, width, height);
            sprites[8] = spriteSheet.getSubimage(90, 27, width, height);
            sprites[9] = spriteSheet.getSubimage(120, 27, width, height);
            sprites[10] = spriteSheet.getSubimage(0, 55, width, height - 1);
            sprites[11] = spriteSheet.getSubimage(30, 55, width, height - 1);
            sprites[12] = spriteSheet.getSubimage(60, 55, width, height - 1);
            sprites[13] = spriteSheet.getSubimage(90, 55, width, height - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites, width, height);
        animation.setDelay(60);

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
