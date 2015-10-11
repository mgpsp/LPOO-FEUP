package maze.logic;

import java.io.Serializable;

/**
 * Represents the fireball.
 */
public class Fireball extends Projectile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1961795882794366809L;

	/**
	 * Constructor for class fireball.
	 * 
	 * @param x x coordinate of the fireball
	 * @param y y coordinate of the fireball
	 * @param horizontal_movement if true, the fireball is moving horizontally
	 * @param positive_movement if true, the fireball is moving to the right or up
	 */
	public Fireball(int x, int y, boolean horizontal_movement,boolean positive_movement){
		super(x,y,horizontal_movement,positive_movement);
	}
	
	/**
	 * Constructor for class fireball.
	 * 
	 * @param xsource x position of entity that spawned the fireball
	 * @param ysource y position of entity that spawned the fireball
	 * @param targetx x position of target
	 * @param targety y position of target
	 */
	public Fireball(int xsource, int ysource, int targetx,int targety){
		super(xsource,ysource, targetx,targety);
	}
	
	/**
	 * Checks the fireball collisions.
	 * <p>
	 * If the fireball hits a wall, the wall is destroyed. If it hits the player, and he has no shield, the player dies.
	 * 
	 * @param g current gamestate
	 * @return true if projectile collided with something
	 */
	public boolean collision(GameState g){
		if(g.getLabyrithm().getTileType(getX(), getY()).equals(Tile.TileType.WALL)){
			if(getX() > 0 && getY() > 0 && getX() < g.getLabyrithm().getWidth()-1 &&getY() < g.getLabyrithm().getHeight()-1)
				g.getLabyrithm().setTile(getX(), getY(), Tile.TileType.FLOOR);
			g.removeProjectile(this);
			return true;
		}
		if(g.getLabyrithm().getTileType(getX(), getY()).equals(Tile.TileType.EXIT)){
			g.removeProjectile(this);
			return true;
		}
		for(Dragon i : g.getDragons()){
			if(projectileCollisionCheck(i)){
				i.projectileHit(this);
				g.removeProjectile(this);
				return true;
			}
		}
		if(projectileCollisionCheck(g.getPlayer())){
			g.getPlayer().projectileHit(this);
			g.removeProjectile(this);
			return true;
		}
		return false;
	}
}
