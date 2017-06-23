package com.github.jarlah.dragontale.tutorial.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import com.github.jarlah.dragontale.tutorial.state.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends Canvas implements Runnable, KeyListener {

    // dimensions
    public static final int GAME_WIDTH = 320;
    public static final int GAME_HEIGHT = 240;
    public static final int GAME_SCALE = 2;

    // game thread
    private Thread thread;
    private final int FPS = 60;
    private final long targetTime = 1000 / FPS;

    // image
    private final BufferedImage image;
    private final Graphics2D g2d;

    // game state manager
    private final GameStateManager gsm;

    public GamePanel() {
        super();
        image = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D) image.getGraphics();
        gsm = new GameStateManager();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        setPreferredSize(new Dimension(GAME_WIDTH * GAME_SCALE, GAME_HEIGHT * GAME_SCALE));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        long start;
        long elapsed;
        long wait;

        // game loop
        while (true) {

            start = System.nanoTime();

            update();
            draw();
            render();

            elapsed = System.nanoTime() - start;

            wait = targetTime - elapsed / 1000000;
            if (wait < 0) {
                wait = 5;
            }

            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
            }
        }
    }

    private void update() {
        gsm.update();
    }

    private void draw() {
        gsm.draw(g2d);
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, GAME_WIDTH * GAME_SCALE, GAME_HEIGHT * GAME_SCALE, null);
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
        bs.show();
    }

    @Override
    public void keyTyped(KeyEvent key) {
    }

    @Override
    public void keyPressed(KeyEvent key) {
        gsm.keyPressed(key.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent key) {
        gsm.keyReleased(key.getKeyCode());
    }

}
