package com.github.jarlah.dragontale.tutorial.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.github.jarlah.dragontale.tutorial.tilemap.TileMap;

@SuppressWarnings("unused")
public class Player extends Actor {
	private static final int PLAYER_WIDTH = 30;
	private static final int PLAYER_HEIGHT = 30;

	// player stuff
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;

	// fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private List<FireBall> fireBalls;

	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;

	// gliding
	private boolean gliding;

	// animations
	private ArrayList<BufferedImage[]> sprites;

	enum AnimationInfo {
		IDLE(0, 2, PLAYER_WIDTH, PLAYER_HEIGHT), WALKING(1, 8, PLAYER_WIDTH,
				PLAYER_HEIGHT), JUMPING(2, 1, PLAYER_WIDTH, PLAYER_HEIGHT), FALLING(
				3, 2, PLAYER_WIDTH, PLAYER_HEIGHT), GLIDING(4, 4, PLAYER_WIDTH,
				PLAYER_HEIGHT), FIREBALL(5, 2, PLAYER_WIDTH, PLAYER_HEIGHT), SCRATCHING(
				6, 5, 60, PLAYER_HEIGHT);

		int index, numFrames, width, height;

		private AnimationInfo(int index, int numFrames, int width, int height) {
			this.index = index;
			this.numFrames = numFrames;
			this.width = width;
			this.height = height;
		}
	}

	public Player(TileMap tm) {

		super(tm);

		width = PLAYER_WIDTH;
		height = PLAYER_HEIGHT;
		cwidth = 20;
		cheight = 20;

		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		facingRight = true;

		health = maxHealth = 5;
		fire = maxFire = 2500;

		fireCost = 200;
		fireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();

		scratchDamage = 8;
		scratchRange = 40;

		// load sprites
		try {

			BufferedImage spritesheet = ImageIO.read(getClass()
					.getClassLoader().getResourceAsStream(
							"Sprites/Player/playersprites.gif"));

			sprites = new ArrayList<BufferedImage[]>();
			AnimationInfo[] animations = AnimationInfo.values();
			for (int i = 0; i < animations.length; i++) {
				AnimationInfo animation = animations[i];
				BufferedImage[] bi = new BufferedImage[animation.numFrames];
				for (int j = 0; j < animation.numFrames; j++) {
					if (AnimationInfo.SCRATCHING != animation) {
						bi[j] = spritesheet.getSubimage(j * width, i * height,
								width, height);
					} else {
						bi[j] = spritesheet.getSubimage(j * width * 2, i
								* height, animation.width, height);
					}
				}
				sprites.add(bi);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		currentAction = AnimationInfo.IDLE.index;
		animation.setFrames(sprites.get(AnimationInfo.IDLE.index));
		animation.setDelay(400);

	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getFire() {
		return fire;
	}

	public int getMaxFire() {
		return maxFire;
	}

	public void setFiring() {
		firing = true;
	}

	public void setScratching() {
		scratching = true;
	}

	public void setGliding(boolean b) {
		gliding = b;
	}

	private void getNextPosition() {

		// movement
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
		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		// cannot move while attacking, except in air
		if ((currentAction == AnimationInfo.SCRATCHING.index || currentAction == AnimationInfo.FIREBALL.index)
				&& !(jumping || falling)) {
			dx = 0;
		}

		// jumping
		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		// falling
		if (falling) {

			if (dy > 0 && gliding)
				dy += fallSpeed * 0.1;
			else
				dy += fallSpeed;

			if (dy > 0)
				jumping = false;
			if (dy < 0 && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed)
				dy = maxFallSpeed;

		}

	}

	public void update() {

		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if (currentAction == AnimationInfo.SCRATCHING.index) {
			if (animation.hasPlayedOnce()) {
				scratching = false;
			}
		}
		
		if (currentAction == AnimationInfo.FIREBALL.index) {
			if (animation.hasPlayedOnce()) {
				firing = false;
			}
		}
		
		fire+=1;
		if(fire > maxFire) fire = maxFire;
		if (firing && currentAction != AnimationInfo.FIREBALL.index) {
			if (fire > fireCost) {
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
			}
		}
		
		Iterator<FireBall> it = fireBalls.iterator();
		while(it.hasNext()){
			FireBall fb = it.next();
			fb.update();
			if (fb.shouldRemove()) {
				it.remove();
			}
		}

		// set animation
		if (scratching) {
			if (currentAction != AnimationInfo.SCRATCHING.index) {
				currentAction = AnimationInfo.SCRATCHING.index;
				animation
						.setFrames(sprites.get(AnimationInfo.SCRATCHING.index));
				animation.setDelay(50);
				width = 60;
			}
		} else if (firing) {
			if (currentAction != AnimationInfo.FIREBALL.index) {
				currentAction = AnimationInfo.FIREBALL.index;
				animation.setFrames(sprites.get(AnimationInfo.FIREBALL.index));
				animation.setDelay(100);
				width = 30;
			}
		} else if (dy > 0) {
			if (gliding) {
				if (currentAction != AnimationInfo.GLIDING.index) {
					currentAction = AnimationInfo.GLIDING.index;
					animation.setFrames(sprites
							.get(AnimationInfo.GLIDING.index));
					animation.setDelay(100);
					width = 30;
				}
			} else if (currentAction != AnimationInfo.FALLING.index) {
				currentAction = AnimationInfo.FALLING.index;
				animation.setFrames(sprites.get(AnimationInfo.FALLING.index));
				animation.setDelay(100);
				width = 30;
			}
		} else if (dy < 0) {
			if (currentAction != AnimationInfo.JUMPING.index) {
				currentAction = AnimationInfo.JUMPING.index;
				animation.setFrames(sprites.get(AnimationInfo.JUMPING.index));
				animation.setDelay(-1);
				width = 30;
			}
		} else if (left || right) {
			if (currentAction != AnimationInfo.WALKING.index) {
				currentAction = AnimationInfo.WALKING.index;
				animation.setFrames(sprites.get(AnimationInfo.WALKING.index));
				animation.setDelay(40);
				width = 30;
			}
		} else {
			if (currentAction != AnimationInfo.IDLE.index) {
				currentAction = AnimationInfo.IDLE.index;
				animation.setFrames(sprites.get(AnimationInfo.IDLE.index));
				animation.setDelay(400);
				width = 30;
			}
		}

		animation.update();

		// set direction
		if (currentAction != AnimationInfo.SCRATCHING.index
				&& currentAction != AnimationInfo.FIREBALL.index) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}

	}

	public void draw(Graphics2D g) {

		setMapPosition();
		
		for(FireBall fb: fireBalls) {
			fb.draw(g);
		}

		// draw player
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) {
				return;
			}
		}

		if (facingRight) {
			g.drawImage(
				animation.getImage(), 
				(int) (x + xmap - width / 2),
				(int) (y + ymap - height / 2), 
				null
			);
		} else {
			g.drawImage(
				animation.getImage(),
				(int) (x + xmap - width / 2 + width),
				(int) (y + ymap - height / 2),
				-width, 
				height, 
				null
			);
		}

	}

}