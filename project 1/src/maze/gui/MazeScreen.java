package maze.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;


import javax.swing.JPanel;

import maze.logic.GameState;

public abstract class MazeScreen extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6648982375672612552L;

	private GameState g;

	private List<Observer> observers = new ArrayList<Observer>();
	
	public MazeScreen(GameState g) throws IOException {
		this.g = g;
		setFocusable(true);
	}
	public void addObserver(Observer o){
		observers.add(o);
	}
	public void notifyObservers(Object obj){
		for(Observer o : observers){
			o.update(null, obj);
		}
	}	
	public GameState getGameState() {
		return g;
	}
}
