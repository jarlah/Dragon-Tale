package com.github.jarlah.dragontale.tutorial.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HUD {

    private Player player;
    private final BufferedImage image;
    private Font font;

    public HUD(Player p) {
        this.player = p;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("HUD/hud.gif"));
            font = new Font("Arial", Font.PLAIN, 14);
        } catch (IOException e) {
            throw new IllegalStateException("Could  not load HUD", e);
        }
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, 0, 10, null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
        g.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100, 30, 45);
    }
}
