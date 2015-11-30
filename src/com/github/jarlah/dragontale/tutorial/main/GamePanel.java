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
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;

	// game thread
	private Thread thread;
	private boolean running, paused;
	private static final double GAME_HERTZ = 60.0;
	private static final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
	private static final double TARGET_FPS = 120;
	private static final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
	private static final int MAX_UPDATES_BEFORE_REDNER = 1;

	// image
	private BufferedImage image;
	private Graphics2D g2d;

	// game state manager
	private GameStateManager gsm;

	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g2d = (Graphics2D) image.getGraphics();
		running = true;
		gsm = new GameStateManager();
		addKeyListener(this);
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();

		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		while (running) {
			double now = System.nanoTime();
			int updateCount = 0;

			if (!paused) {
				while (shouldUpdate(lastUpdateTime, now, updateCount)) {
					update();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

				if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				draw();

				render();

				lastRenderTime = now;

				int thisSecond = (int) (lastUpdateTime / 1000000000);
				if (thisSecond > lastSecondTime) {
					lastSecondTime = thisSecond;
				}

				while (shouldWait(lastUpdateTime, lastRenderTime, now)) {
					Thread.yield();
					try {
						Thread.sleep(1);
					} catch (Exception e) {
					}
					now = System.nanoTime();
				}
			}
		}
	}

	private boolean shouldWait(double lastUpdateTime, double lastRenderTime, double now) {
		return (now - lastRenderTime) < TARGET_TIME_BETWEEN_RENDERS && (now - lastUpdateTime) < TIME_BETWEEN_UPDATES;
	}

	private boolean shouldUpdate(double lastUpdateTime, double now, int updateCount) {
		return (now - lastUpdateTime) > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_REDNER;
	}

	private void update() {
		gsm.update();
	}

	/**
	 * Draws on the cached graphics object
	 */
	private void draw() {
		gsm.draw(g2d);
	}

	/**
	 * Does the actual rendering to the screen
	 */
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
		bs.show();
	}

	public void keyTyped(KeyEvent key) {
	}

	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key.getKeyCode());
	}

	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key.getKeyCode());
	}

}
