package pacman;

import javax.swing.*;

public class GameFrame extends JFrame{
	
	public GameFrame() {
		setResizable(false);
		add(new GamePanel());
	}
	public static void GamePlay() {
		GameFrame pac = new GameFrame();
		pac.setVisible(true);
		pac.setTitle("Pacman");
		pac.setSize(518,580);
		
		pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pac.setLocationRelativeTo(null);
	}

}
