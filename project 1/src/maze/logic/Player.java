package maze.logic;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class Player extends Entity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -984920561763678332L;

	/**
	 * constructor for player character
	 * @param x x position of player
	 * @param y y position of player
	 */
	public Player(int x, int y){
		super(x,y);
	}
	
	/**
	 * get the players equipment
	 * @return equipped items
	 */
	public ArrayList<Equipment> getEquipped() {
		return equipped;
	}

	/**
	 * take an item  if it can be equipped
	 * if it is a sword, become armed, if it is a shield become protected, if it is a quiver, increase number of available darts
	 * @param i item to take
	 */
	public void takeItem(Item i){
		if(!i.getDraw())
			return;
		i.setDraw(false);
		if(i instanceof Equipment){
			if(i instanceof DartQuiver){
				DartQuiver d = getDartQuiver();
				if(d != null){
					d.addDarts(((DartQuiver)i).getNumdarts());
					return;
				}
			}
		}
		equipped.add((Equipment)i);
	}
	
	/**
	 * get the players equipped quiver
	 * @return players quiver
	 */
	public DartQuiver getDartQuiver(){
		for(Equipment it: equipped){
			if(it instanceof DartQuiver)
				return (DartQuiver)it;
		}
		return null;
	}
	
	/**
	 * get the number of darts the player character has
	 * @return number of darts the player has
	 */
	public int getNumDarts(){
		DartQuiver d = getDartQuiver();
		if(d==null)
			return 0;
		return d.getNumdarts();
	}
	
	/**
	 * move player character to position
	 * @param x x of destination
	 * @param y y of destination
	 * @param g gamestate to move on
	 * @return true if player managed to move
	 */
	public boolean move(int x, int y, GameState g)	{
		Labyrithm l = g.getLabyrithm();
		int newx = getX() + x;
		int newy = getY() + y;
		if(newx < 0 || newy < 0 || newx >= l.getWidth() || newy >= l.getHeight())
			return false;
		if(l.getTileType(newx, newy).equals(Tile.TileType.WALL))
			return false;
		if(l.getTileType(newx, newy).equals(Tile.TileType.EXIT) && (!isArmed()|| g.getLiveDragons() != 0))
			return false;
		
		setXY(newx, newy);

		
		for(Item it : g.getItems())
		{
			if(it.getDraw() && it.getX() == getX() && it.getY() == getY()){
				takeItem(it);
			}
		}
		return true;
	}
	
	/**
	 * check whether the player is armed with a sword
	 * @return true if player is armed
	 */
	public boolean isArmed(){
		for(Equipment i : equipped){
			if(i instanceof Sword)
				return true;
		}
		return false;
	}
	
	/**
	 * check whether player has a shield
	 * @return true if player has a shield
	 */
	public boolean isShielded(){
		for(Equipment i : equipped){
			if(i instanceof Shield)
				return true;
		}
		return false;
	}
	
	/**
	 * resolve player being hit with a projectile
	 * @param p projectile hitting the player
	 * @return true if player died
	 */
	public boolean projectileHit(Projectile p){
		if(p != null && ! (p instanceof Dart) && !isShielded()){
			setDraw(false);
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * throw a dart in a given direction
	 * @param x distance in x to the spawning of the dart
	 * @param y distance in y to the spawning of the dart 
	 * @param g gamestate to throw in
	 * @return true if dart was thrown
	 */
	public boolean throwDart(int x, int y, GameState g){
		if(x == 0 && y == 0)
			throw new IllegalArgumentException();
		DartQuiver d;
		if((d=getDartQuiver()) == null)
			return false;
		g.addProjectiles(new Dart(getX()+x, getY()+y, x!=0, (x>0)||(y>0)));
		d.decNumDarts();
		if(d.getNumdarts() == 0)
			equipped.remove(d);
		return true;
	}
	
	private ArrayList<Equipment> equipped = new ArrayList<Equipment>();
}
