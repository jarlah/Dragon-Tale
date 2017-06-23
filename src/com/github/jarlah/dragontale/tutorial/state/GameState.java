package com.github.jarlah.dragontale.tutorial.state;

import java.awt.Graphics2D;

public abstract class GameState {

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    protected GameStateManager gsm;

    public abstract void update();

    public abstract void draw(Graphics2D g);

    public abstract void keyPressed(int e);

    public abstract void keyReleased(int e);
}
