package maze.logic;

import java.io.Serializable;

/**
 * Abstract class that represents the equipment (shield and sword).
 */
public abstract class Equipment extends Item implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5692830537980276993L;

	/**
	 * Constructor for class Equipment.
	 * 
	 * @param x x coordinate of the equipment
	 * @param y y coordinate of the equipment
	 */
	public Equipment(int x, int y){
		super(x,y);
	}
}
