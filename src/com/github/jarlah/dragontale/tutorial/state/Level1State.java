package com.github.jarlah.dragontale.tutorial.state;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.github.jarlah.dragontale.tutorial.audio.AudioPlayer;
import com.github.jarlah.dragontale.tutorial.entity.Enemy;
import com.github.jarlah.dragontale.tutorial.entity.Explosion;
import com.github.jarlah.dragontale.tutorial.entity.HUD;
import com.github.jarlah.dragontale.tutorial.entity.Player;
import com.github.jarlah.dragontale.tutorial.entity.enemies.Bahamut;
import com.github.jarlah.dragontale.tutorial.entity.enemies.RedBird;
import com.github.jarlah.dragontale.tutorial.entity.enemies.Slugger;
import com.github.jarlah.dragontale.tutorial.main.GamePanel;
import com.github.jarlah.dragontale.tutorial.tilemap.Background;
import com.github.jarlah.dragontale.tutorial.tilemap.TileMap;

public class Level1State extends GameState {
	private TileMap tileMap;
	private Background bg;

	private Player player;
	
	private List<Enemy> enemies;
	
	private List<Explosion> explosions;
	
	private HUD hud;
	
	private AudioPlayer bgMusic;

	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		tileMap = new TileMap(30);
		tileMap.loadTiles("Tilesets/grasstileset.gif");
		tileMap.loadMap("Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.07);

		bg = new Background("Backgrounds/grassbg1.gif", 0.1);

		player = new Player(tileMap);
		player.setPosition(400, 100);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();

		hud = new HUD(player);
		
		bgMusic = new AudioPlayer("Music/level1-1.mp3");
		bgMusic.play();
	}
	
	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		
		for (Point point: new Point[] {
			new Point(200, 100),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200)
		}) {
			Slugger s = new Slugger(tileMap);
			s.setPosition(point.getX(), point.getY());
			enemies.add(s);
		}	
		
		RedBird s = new RedBird(tileMap);
		s.setPosition(100, 50);
		enemies.add(s);
		
		Bahamut b = new Bahamut(tileMap);
		b.setPosition(100, 50);
		enemies.add(b);
	}

	public void update() {
		player.update();
		
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		player.checkAttack(enemies);
		
		updateEnemies();
		
		updateExplosions();
	}

	private void updateEnemies() {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if (e.isDead()) {
				enemies.remove(i);
				i--;
				final Explosion expl = new Explosion(e.getx(), e.gety());
				expl.setMapPosition((int)e.getXmap(),(int)e.getYmap());
				explosions.add(expl);
			}
		}
	}

	private void updateExplosions() {
		for (int i = 0; i < explosions.size(); i++) {
			Explosion e = explosions.get(i);
			e.update();
			if (e.shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
	}

	public void draw(Graphics2D g) {
		// draw bg
		bg.draw(g);

		// draw tilemap
		tileMap.draw(g);

		for (Enemy en: enemies) {
			en.draw(g);
		}
		
		// draw player
		player.draw(g);

		for (Explosion expl: explosions) {
			expl.draw(g);
		}
		
		hud.draw(g);
	}

	public void keyPressed(int k) {
		if (k == KeyEvent.VK_LEFT)
			player.setLeft(true);
		if (k == KeyEvent.VK_RIGHT)
			player.setRight(true);
		if (k == KeyEvent.VK_UP)
			player.setUp(true);
		if (k == KeyEvent.VK_DOWN)
			player.setDown(true);
		if (k == KeyEvent.VK_W)
			player.setJumping(true);
		if (k == KeyEvent.VK_E)
			player.setGliding(true);
		if (k == KeyEvent.VK_R)
			player.setScratching();
		if (k == KeyEvent.VK_F)
			player.setFiring();
	}

	public void keyReleased(int k) {
		if (k == KeyEvent.VK_LEFT)
			player.setLeft(false);
		if (k == KeyEvent.VK_RIGHT)
			player.setRight(false);
		if (k == KeyEvent.VK_UP)
			player.setUp(false);
		if (k == KeyEvent.VK_DOWN)
			player.setDown(false);
		if (k == KeyEvent.VK_W)
			player.setJumping(false);
		if (k == KeyEvent.VK_E)
			player.setGliding(false);
	}
}
