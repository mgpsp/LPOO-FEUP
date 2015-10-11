package maze.logic;

import java.io.Serializable;

/**
 * represents the sword in the game
 */
public class Sword extends Equipment  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9149760487945849502L;

	/**
	 * constructor for sword
	 * @param x x position of sword
	 * @param y y position of sword
	 */
	public Sword(int x, int y){
		super(x,y);
	}
}
