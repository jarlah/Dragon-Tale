package com.github.jarlah.dragontale.tutorial.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.github.jarlah.dragontale.tutorial.audio.AudioPlayer;
import com.github.jarlah.dragontale.tutorial.tilemap.Background;

public class GameOverState extends GameState {
	
	private Color titleColor;
	private Font titleFont;
	private AudioPlayer bgMusic;
	private Background bg;

	public GameOverState(GameStateManager gsm) {
		super(gsm);
		try {
			bg = new Background("Backgrounds/gameover.jpg", 1);
			bg.setVector(0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		titleColor = new Color(128, 0, 0);
		titleFont = new Font("Century Gothic", Font.BOLD, 30);
		bgMusic = new AudioPlayer("SFX/gameover.mp3", -7f);
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
		g.drawString("GAME OVER", 70, 120);
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
