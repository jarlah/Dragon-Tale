package com.github.jarlah.dragontale.tutorial.main;

import javax.swing.JFrame;

public class Game {
	public static void main(String[] args) {
		JFrame window = new JFrame("Dragon Tale");
		GamePanel panel = new GamePanel();
		window.add(panel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setFocusable(true);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.requestFocus();
		panel.requestFocus();
	}
}
