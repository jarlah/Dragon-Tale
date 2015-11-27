package com.github.jarlah.dragontale.tutorial.state;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class GameStateManager {
	private List<GameState> gameStates;
	private int currentState;
	
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	
	public GameStateManager() {
		gameStates = new ArrayList<>();
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
	}
	
	public void setState(int state) {
		currentState = state;
		gameStates.get(currentState).init();
	}
	
	public void update() {
		gameStates.get(currentState).update();
	}
	
	public void draw(Graphics2D g) {
		gameStates.get(currentState).draw(g);
	}
	
	public void keyPressed(int e) {
		gameStates.get(currentState).keyPressed(e);
	}
	
	public void keyReleased(int e) {
		gameStates.get(currentState).keyReleased(e);
	}
}
