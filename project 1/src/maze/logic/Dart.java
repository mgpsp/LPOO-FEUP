package maze.logic;

import java.io.Serializable;

/**
 * Represents the dart.
 * <p>
 * The dart moves up, down, left and right.
 */
public class Dart extends Projectile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1895062174004993945L;

	/**
	 * Constructor for class Dart.
	 * 
	 * @param x x coordinate of the dart
	 * @param y y coordinate of the dart
	 * @param horizontal_movement true if the dart is moving horizontally
	 * @param positive_movement true if the dart if moving to the right
	 */
	public Dart(int x, int y, boolean horizontal_movement,boolean positive_movement){
		super(x,y,horizontal_movement,positive_movement);
	}

	/**
	 * Checks if the dart collided with a wall or a dragon.
	 * <p>
	 * If it collides with a wall, the dart is deleted. If a dragon gets hit, it dies. 
	 * 
	 * @param g gamestate to check for collision
	 */
	public boolean collision(GameState g){
		if(g.getLabyrithm().getTileType(getX(), getY()).equals(Tile.TileType.EXIT) || g.getLabyrithm().getTileType(getX(), getY()).equals(Tile.TileType.WALL)){
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
		return false;
	}
}
