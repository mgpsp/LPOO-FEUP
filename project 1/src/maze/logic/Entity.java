package maze.logic;

import java.io.Serializable;
import java.util.Comparator;

import maze.logic.GameState;

/**
 * Abstract class that represents an entity.
 * 
 */
public abstract class Entity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 377664309242306492L;

	/**
	 * Resets the previous position.
	 */
	public void resetPrevious(){
		previousx = -1;
		previousy = -1;
	}
	
	/**
	 * Gets the previous x coordinate.
	 * @return returns the previous x coordinate
	 */
	public int getPreviousx() {
		return previousx;
	}
	
	/**
	 * Gets the previous y coordinate.
	 * @return returns the previous y coordinate
	 */
	public int getPreviousy() {
		return previousy;
	}
	private boolean draw;
	
	/**
	 * Constructor for class entity.
	 * 
	 * @param x x coordinate of the entity
	 * @param y y coordinate of the entity
	 */
	public Entity(int x, int y){
		this.x = x;
		this.y = y;
		draw = true;
		previousx = -1;
		previousy = -1;
	}

	/**
	 * Gets the x coordinate of the entity.
	 * 
	 * @return returns the x coordinate of the entity
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Sets the x coordinate of the entity
	 * @param x new x coordinate
	 * @param g gamestate to move in
	 */
	public void setX(int x, GameState g) {
		previousx = this.x;
		previousy = this.y;
		this.x = x;
	}
	
	/**
	 * Sets the x coordinate of the entity.
	 * 
	 * @param x x coordinate of the entity
	 */
	public void setX(int x) {
		previousx = this.x;
		previousy = this.y;
		this.x = x;
	}
	
	/**
	 * Gets the y coordinate of the entity.
	 * 
	 * @return returns the y coordinate of the entity
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * set y of entity
	 * @param y new y coordinate
	 * @param g gamestate to move in
	 */
	public void setY(int y, GameState g) {
		previousx = this.x;
		previousy = this.y;
		this.y = y;
	}
	
	/**
	 * Sets the y coordinate of the entity.
	 * 
	 * @param y y coordinate of the entity
	 */
	public void setY(int y) {
		previousx = this.x;
		previousy = this.y;
		this.y = y;
	}
	
	/**
	 * Sets the position of the entity.
	 * 
	 * @param x x coordinate of the entity
	 * @param y y coordinate of the entity
	 */
	public void setXY(int x, int y){
		previousx = this.x;
		previousy = this.y;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Determine whether entity should be drawn
	 * 
	 * @return returns true if draw is true
	 */
	public boolean getDraw() {
		return draw;
	}
	
	/**
	 * Sets draw.
	 * 
	 * @param draw attribute to set
	 */
	public void setDraw(boolean draw) {
		this.draw = draw;
	}	
	/**
	 * Compares two objects.
	 * 
	 * @param o returns true if the given object equals the current object
	 */
	public boolean equals(Object o){
		return o != null && o instanceof Entity && this.x == ((Entity)o).x && this.y == ((Entity)o).y;
	}
	
	/**
	 *	Determine order of entity printing
	 */
	public static class EntityPositionComparator implements Comparator<Entity>
	{
	    @Override
	    public int compare(Entity e1, Entity e2)
	    {
	    	if(e1 == null && e2 == null)
	    		return 0;
	    	if(e1 == null)
	    		return -1;
	    	if(e2==null)
	    		return 1;
	    	if(e1.x < e2.x )
	    		return -1;
	    	if(e1.x > e2.x)
	    		return 1;
	    	if(e1.y < e2.y)
	    		return -1;
	    	if(e1.y > e2.y)
	    		return 1;
	    	if(e1 instanceof Projectile)
	    		return 1;
	    	if(e2 instanceof Projectile)
	    		return -1;
	    	if(e1 instanceof Player)
	    		return 1;
	    	if(e2 instanceof Player)
	    		return -1;
	    	if(e1 instanceof Dragon)
	    		return 1;
	    	if(e2 instanceof Dragon)
	    		return -1;
	    	return 0;
	    }
	}
	private int x, y;
	private int previousx = -1;
	private int previousy = -1;
	
}
