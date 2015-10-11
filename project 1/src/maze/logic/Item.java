package maze.logic;

import java.io.Serializable;

/**
 *
 */
public abstract class Item extends Entity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 862538077045914773L;

	/**
	 * item constructor
	 * @param x x position of item to create
	 * @param y y position of item to create
	 */
	public Item(int x, int y){
		super(x,y);
	}
}
