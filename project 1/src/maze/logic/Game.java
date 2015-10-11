package maze.logic;

import java.io.IOException;

import javax.swing.SwingUtilities;

import maze.cli.Cli;
import maze.gui.MazeWindow;

public class Game {
	public static void main(String[] args) throws IOException {
		for(String s : args){
			if(s.equals("CLI"))
			{
				GameState g = Cli.setup();
				while(!GameLogic.winCondition(g))
				{
					Cli.printGame(g);
					GameLogic.Movement input = Cli.input(g);
					if(GameLogic.gameLoop(g, input))
						break;
				}
				Cli.printGame(g);
				Cli.printEnd(GameLogic.winCondition(g));
				Cli.closeInput();
				return;
			}
		}
		 SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	try {
						@SuppressWarnings("unused")
						MazeWindow window = MazeWindow.getInstance();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	        });
		
	}
}
