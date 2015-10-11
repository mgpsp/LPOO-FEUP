package maze.logic;

import java.io.Serializable;

/**
 * represents the shield in the game
 */
public class Shield extends Equipment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8431235235024697903L;

	/**
	 * constructor for shield
	 * @param x x position of shield
	 * @param y y position of shield
	 */
	public Shield(int x, int y){
		super(x,y);
	}
}
