package maze.logic;

import java.io.Serializable;

/**
 *	class representing a projectile
 */
public abstract class Projectile extends Entity  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4743099855375277179L;
	/**
	 * Constructor for class
	 * @param x x position of projectile
	 * @param y y position of projectile
	 * @param horizontal_movement indicates whether the trajectory of the projectile is horizontal or vertical
	 * @param positive_movement indicates whether the projectile moves positively
	 */
	public Projectile(int x, int y, boolean horizontal_movement,boolean positive_movement){
		super(x,y);
		this.horizontal_movement=horizontal_movement;
		this.positive_movement=positive_movement;
	}
	
	/**
	 * constructor for class
	 * @param xsource x position of origin of projectile
	 * @param ysource y position of origin of projectile
	 * @param targetx x position of target of projectile
	 * @param targety y position of target of projectile
	 */
	public Projectile(int xsource, int ysource, int targetx,int targety){
		super(xsource,ysource);
		int dirx= targetx - xsource;
		int diry = targety - ysource;
		if(dirx != 0 && diry != 0)
			throw new IllegalArgumentException();
		if(dirx != 0){
			if(dirx > 0){
				setX(xsource+1);
				this.horizontal_movement=true;
				this.positive_movement=true;
			}
			else{
				setX(xsource-1);
				this.horizontal_movement=true;
				this.positive_movement=false;
			}	
		}
		else{
			if(diry > 0){
				setY(ysource+1);
				this.horizontal_movement=false;
				this.positive_movement=true;
			}
			else{
				setY(ysource-1);
				this.horizontal_movement=false;
				this.positive_movement=false;
			}	
		}
		resetPrevious();
	}

	/**
	 * @return true if movement of projectile is horizontal
	 */
	public boolean getHorizontal_movement() {
		return horizontal_movement;
	}
	
	/**
	 * modify direction of projectile movement 
	 * @param horizontal_movement new value for direction of movement
	 */
	public void setHorizontal_movement(boolean horizontal_movement) {
		this.horizontal_movement = horizontal_movement;
	}
	
	/**
	 * @return true if movement of projectile is positive
	 */
	public boolean getPositive_movement() {
		return positive_movement;
	}
	
	/**
	 * modify direction of projectile movement
	 * @param positive_movement new value of projectile movement
	 */
	public void setPositive_movement(boolean positive_movement) {
		this.positive_movement = positive_movement;
	}
	
	/**
	 * move projectile according to its direction
	 */
	public void move(){
		if(horizontal_movement){
			if(positive_movement)
				setX(getX()+1);
			else
				setX(getX()-1);
		}
		else {
			if(positive_movement)
				setY(getY()+1);
				else
					setY(getY()-1);
		}
	}
	
	/**
	 * resolve collision with other objects
	 * @param g gamestate
	 * @return true if projectile collided
	 */
	public abstract boolean collision(GameState g);
	
	/**
	 * check collision with other entity
	 * @param e entity to check for collision
	 * @return true if this projectile collided with entity
	 */
	public boolean projectileCollisionCheck(Entity e){
		return e.getDraw() && (getX() == e.getX() && getY() == e.getY()) ||((getPreviousx() == e.getX() && getPreviousy() == e.getY()) && (e.getPreviousx() == getX() && e.getPreviousy() == getY()));
	}
	
	private boolean horizontal_movement;
	private boolean positive_movement;

}
