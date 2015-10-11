package maze.logic;
import java.io.Serializable;
import java.util.Random;

/**
 * Represents the dragon
 */
public class Dragon extends Entity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8447357694775872433L;
	public enum DragonBehaviour {STATIONARY, MOVING, SLEEPING};
	
	
	
	/**
	 * Gets the dragon behaviour.
	 * <p>
	 * The dragon can be stationary, moving or sleeping.
	 * @return the dragon behaviour
	 */
	public static DragonBehaviour getBehaviour() {
		return behaviour;
	}
	
	/**
	 * Sets the dragon behaviour.
	 * <p>
	 * The dragon can be stationary, moving or sleeping.
	 * @param behaviour behaviour to set
	 */
	public static void setBehaviour(DragonBehaviour behaviour) {
		Dragon.behaviour = behaviour;
	}
	
	/**
	 * Constructor for class Dragon
	 * @param x x coordinate of the dragon
	 * @param y y coordinate of the dragon
	 */
	public Dragon(int x, int y){
		super(x,y);
		asleep = false;
	}
	
	/**
	 * Get dragon's asleep.
	 * 
	 * @return returns true if the dragon is asleep
	 */
	public boolean getAsleep() {
		return asleep;
	}
	
	/**
	 * Sets dragon's asleep
	 * @param asleep true to set dragon as asleep
	 */
	public void setAsleep(boolean asleep) {
		this.asleep = asleep;
	}
	
	/**
	 * Increments sleep cycles
	 * 
	 * @param sleepcycles number of cycles to increment
	 */
	public void sleep(int sleepcycles){
		asleep = true;
		this.sleepcycles += sleepcycles;
	}
	
	/**
	 * Move the dragon to the given position
	 * 
	 * @param x x coordinate of the new position
	 * @param y y coordinate of the new position
	 * @param g current gamestate
	 * @return returns true if it's possible to move the dragon to the given position
	 */
	public boolean move(int x, int y,  GameState g){
		 Labyrithm l = g.getLabyrithm();
		if(behaviour == DragonBehaviour.STATIONARY || !getDraw())
			return false;
		
		int newx = getX() + x;
		int newy = getY()+ y;
		if(newx < 0 || newy < 0 || newx >= l.getWidth() || newy >= l.getHeight())
			return false;
		if(l.getTileType(newx, newy).equals(Tile.TileType.WALL))
			return false;
		if(l.getTileType(newx, newy).equals(Tile.TileType.EXIT))
			return false;
		setXY(newx, newy);
		
		return true;
	}
	
	/**
	 * Gets the sleep cycles, number of turns before the dragon wakes up
	 * 
	 * @return returns the number of sleep cycles
	 */
	public int getSleepcycles() {
		return sleepcycles;
	}
	
	/**
	 * Sets the sleep cycles
	 * 
	 * @param sleepcycles number of sleep cycles to set
	 */
	public void setSleepcycles(int sleepcycles) {
		this.sleepcycles = sleepcycles;
	}
	
	/**
	 * Random dragon move
	 * 
	 * @param g gamestate to movein
	 * @return success of move
	 */
	public boolean move(GameState g){
		Random r = new Random();
		if(behaviour.equals(DragonBehaviour.SLEEPING)){
			if(asleep){
				sleepcycles--;
				if(sleepcycles == 0)
					setAsleep(false);
				else return false;
			}
				if(r.nextDouble() < SLEEP_PROBABILITY){
					setAsleep(true);
					setSleepcycles(r.nextInt(MAX_SLEEP-MIN_SLEEP+1)+MIN_SLEEP);
					return false;
				}	
		}
		int x=0;
		int y=0;
		if(r.nextBoolean())
		{
			if(r.nextBoolean())
				x= 1;
			else x=-1;
		}
		else{
			if(r.nextBoolean())
				y= 1;
			else y=-1;
		}
		boolean returnvalue =  move(x, y, g);
		if(!asleep && getDraw()){
			if(spitFire(g))
				cooldown = true;
			else cooldown = false;
		}
		return returnvalue;
	}
	
	/**
	 * Defined dragon move
	 * @param x x position
	 * @param y y position
	 * @param g gamestate
	 * @return success of operation
	 */
	public boolean directedMove(int x, int y, GameState g){
		if(behaviour.equals(DragonBehaviour.SLEEPING)){
			if(asleep){
				sleepcycles--;
				if(sleepcycles == 0)
					setAsleep(false);
				else return false;
			}
		}
		boolean returnvalue =  move(x, y, g);
		if(!asleep && getDraw()){
			if(spitFire(g))
				cooldown = true;
			else cooldown = false;
		}
		return returnvalue;
	}
	
	/**
	 * Based on the player's and dragon's positions, determine whether the dragon should spit a fireball or not.
	 * @param g current gamestate
	 * @return true if dragon spit fire
	 */
	public boolean spitFire(GameState g){
		if(cooldown)
			return false;
		if(
				!(
						(g.getPlayer().getX() == getX() && Math.abs(g.getPlayer().getY()- getY()) <= SPIT_DISTANCE && Math.abs(g.getPlayer().getY()- getY()) > 1) || 
						(g.getPlayer().getY() == getY() && Math.abs(g.getPlayer().getX()- getX()) <= SPIT_DISTANCE && Math.abs(g.getPlayer().getX()- getX()) > 1)
						)
		  )
			return false;
		if(g.getPlayer().getX() == getX()){
			if(g.getPlayer().getY() > getY()){
				for(int i = getY()+1; i < g.getPlayer().getY(); i++){
					if(g.getLabyrithm().getTileType(getX(), i).equals(Tile.TileType.WALL))
						return false;
				}
					g.addProjectiles(new Fireball(getX(), getY(), g.getPlayer().getX(), g.getPlayer().getY()));
				
			}
			else{
				for(int i = getY()-1; i > g.getPlayer().getY(); i--){
					if(g.getLabyrithm().getTileType(getX(), i).equals(Tile.TileType.WALL))
						return false;
				}
					g.addProjectiles(new Fireball(getX(), getY(), g.getPlayer().getX(), g.getPlayer().getY()));
			}
		}
		else {
			if(g.getPlayer().getX() > getX()){
				for(int i = getX()+1; i < g.getPlayer().getX(); i++){
					if(g.getLabyrithm().getTileType(i, getY()).equals(Tile.TileType.WALL))
						return false;
				}
					g.addProjectiles(new Fireball(getX(), getY(), g.getPlayer().getX(), g.getPlayer().getY()));
			}
			else{
				for(int i = getX()-1; i > g.getPlayer().getX(); i--){
					if(g.getLabyrithm().getTileType(i, getY()).equals(Tile.TileType.WALL))
						return false;
				}
					g.addProjectiles(new Fireball(getX(), getY(), g.getPlayer().getX(), g.getPlayer().getY()));
			}
		}
		return true;
	}
	
	/**
	 * Compares two dragons
	 * @param o object to compare
	 * @return returns true if the given object equals the current object
	 */
	public boolean equals(Object o){
		return o != null && o instanceof Dragon && super.equals(o) && this.asleep == ((Dragon)o).asleep && this.sleepcycles == ((Dragon)o).sleepcycles;
	}
	
	/**
	 * Determine result of projectile hitting dragon
	 * @param p projectile
	 * @return true if dragon is hit by projectile
	 */
	public boolean projectileHit(Projectile p){
		if(p != null /*&& !(p instanceof Fireball)*/){
			setDraw(false);
			return true;
		}
		else 
			return false;
	}
	
	private static final double SLEEP_PROBABILITY = .05;
	private static final int SPIT_DISTANCE = 3;
	private static final int MIN_SLEEP = 2;
	private static final int MAX_SLEEP = 5;
	private boolean asleep;
	private int sleepcycles;
	private boolean cooldown;
	private static DragonBehaviour behaviour = DragonBehaviour.SLEEPING;
}
