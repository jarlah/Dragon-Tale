package com.github.jarlah.dragontale.tutorial.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import com.github.jarlah.dragontale.tutorial.state.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends Canvas implements Runnable, KeyListener {

	// dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;

	// game thread
	private Thread thread;
	private volatile boolean running = false;
	private volatile boolean paused = false;
	
	// game state
	private GameStateManager gsm;
	
	// image
	private BufferedImage image;
	private Graphics2D g2d;

	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	final double GAME_HERTZ = 30.0;
	final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
	final double TARGET_FPS = 120;
	final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
	final int MAX_UPDATES_BEFORE_REDNER = 5;
	
	@Override
	public void run() {
		init();
		
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();
		
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		while (running) {
			double now = System.nanoTime();
			int updateCount = 0;

			if (!paused) {
				while (now - lastUpdateTime > TIME_BETWEEN_UPDATES
						&& updateCount < MAX_UPDATES_BEFORE_REDNER) {
					update();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

				if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				draw();
				
				drawToScreen();

				lastRenderTime = now;

				int thisSecond = (int) (lastUpdateTime / 1000000000);
				if (thisSecond > lastSecondTime) {
					lastSecondTime = thisSecond;
				}

				while ((now - lastRenderTime) < TARGET_TIME_BETWEEN_RENDERS
						&& (now - lastUpdateTime) < TIME_BETWEEN_UPDATES) {
					Thread.yield();
					try {Thread.sleep(1);} catch (Exception e) {}
					now = System.nanoTime();
				}
			}
		}
	}

	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g2d = (Graphics2D) image.getGraphics();
		running = true;
		gsm = new GameStateManager();
	}

	private void drawToScreen() {
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
	}

	private void draw() {
		gsm.draw(g2d);
	}

	private void update() {
		gsm.update();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		gsm.keyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		gsm.keyReleased(e.getKeyCode());
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
