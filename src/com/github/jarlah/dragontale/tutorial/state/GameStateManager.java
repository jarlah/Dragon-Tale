package com.github.jarlah.dragontale.tutorial.state;


public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;

	public static final int NUMGAMESTATES = 4;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int LEVELCOMPLETE = 2;
	public static final int GAMEOVER = 3;

	public GameStateManager() {

		gameStates = new GameState[NUMGAMESTATES];

		currentState = MENUSTATE;
		
		loadState(currentState);
	}

	private void loadState(int state) {
		if (state == MENUSTATE) {
			gameStates[state] = new MenuState(this);
		}
		if (state == LEVEL1STATE) {
			gameStates[state] = new Level1State(this);
		}
		if (state == LEVELCOMPLETE) {
			gameStates[state] = new LevelCompleteState(this);
		}
		if (state == GAMEOVER) {
			gameStates[state] = new GameOverState(this);
		}
	}
	
	public void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
		unloadState(state);
		currentState = state;
		loadState(state);
	}

	public void update() {
		if (gameStates[currentState] == null) return;
		gameStates[currentState].update();
	}

	public void draw(java.awt.Graphics2D g) {
		if (gameStates[currentState] == null) return;
		gameStates[currentState].draw(g);
	}

	public void keyPressed(int k) {
		if (gameStates[currentState] == null) return;
		gameStates[currentState].keyPressed(k);
	}

	public void keyReleased(int k) {
		if (gameStates[currentState] == null) return;
		gameStates[currentState].keyReleased(k);
	}

}