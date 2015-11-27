package com.github.jarlah.dragontale.tutorial.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.github.jarlah.dragontale.tutorial.tilemap.Background;

public class MenuState extends GameState {
	private Background bg;

	private int currentChoice = 0;
	private String[] options = new String[] { "Start", "Help", "Quit" };

	private Color titleColor;
	private Font titleFont;
	private Font font;

	public MenuState(GameStateManager gameStateManager) {
		this.gsm = gameStateManager;
		try {
			bg = new Background("Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1, 0);
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			font = new Font("Arial", Font.PLAIN, 12);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

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
		g.drawString("Dragon Tale", 80, 70);
		g.setFont(font);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 145, 140 + i * 15);
		}
	}

	@Override
	public void keyPressed(int e) {
		if (e == KeyEvent.VK_ENTER) {
			select();
		}
		if (e == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if (e == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}

	private void select() {
		switch (currentChoice) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(int e) {
		// TODO Auto-generated method stub

	}

}
