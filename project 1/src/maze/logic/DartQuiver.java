package maze.logic;

import java.io.Serializable;

/**
 * Represents the dart quiver.
 */
public class DartQuiver extends Equipment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3516789688223086244L;

	/**
	 * Constructor for class DartQuiver.
	 * 
	 * @param x x coordinate of the dart quiver
	 * @param y y coordinate of the dart quiver
	 * @param numdarts number of darts in the quiver
	 */
	public DartQuiver(int x, int y, int numdarts){
		super(x,y);
		this.numdarts= numdarts;
	}
	
	/**
	 * Get number of darts in the dart quiver
	 * @return number of darts in the dart quiver
	 */
	public int getNumdarts() {
		return numdarts;
	}
	
	/**
	 * Set number of darts in the dart quiver
	 * @param numdarts new number of darts
	 */
	public void setNumdarts(int numdarts) {
		this.numdarts = numdarts;
	}
	
	/**
	 * Add darts to the dart quiver
	 * @param numdarts number of darts to add
	 */
	public void addDarts(int numdarts) {
		this.numdarts += numdarts;
	}
	
	
	/**
	 * Remove one dart from the dart quiver
	 * @return true if player still has darts
	 */
	public boolean decNumDarts(){
		if(numdarts == 0)
			return false;
		numdarts--;
		return true;
	}
	
	private int numdarts = 0;
}
