package com.github.jarlah.dragontale.tutorial.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.github.jarlah.dragontale.tutorial.audio.AudioPlayer;
import com.github.jarlah.dragontale.tutorial.tilemap.Background;

public class LevelCompleteState extends GameState {

    private final Background bg;
    private final Color titleColor;
    private final Font titleFont;
    private final AudioPlayer bgMusic;

    public LevelCompleteState(GameStateManager gsm) {
        super(gsm);
        try {
            bg = new Background("Backgrounds/complete.png", 1);
            bg.setVector(0, 0);
        } catch (Exception e) {
            throw new IllegalStateException("Could not load complete.png", e);
        }
        titleColor = Color.BLACK;
        titleFont = new Font("Arial", Font.BOLD, 30);
        bgMusic = new AudioPlayer("SFX/hurra.mp3", -7f);
        bgMusic.play();
    }

    @Override
    public void update() {
        bg.update();
    }

    @Override
    public void draw(Graphics2D g) {
        bg.draw(g);

        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("LEVEL CLEARED", 40, 120);
    }

    @Override
    public void keyPressed(int e) {
        if (e == KeyEvent.VK_ENTER) {
            bgMusic.stop();
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    @Override
    public void keyReleased(int e) {
        // TODO Auto-generated method stub

    }

}
