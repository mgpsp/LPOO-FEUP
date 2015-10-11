package maze.logic;

import java.util.*;

/**
 * Contains the game logic.
 */
public class GameLogic {
	
	private GameLogic(){};
	public enum Movement {UP, DOWN, LEFT,RIGHT, NONE, THROW_UP, THROW_DOWN, THROW_LEFT, THROW_RIGHT,RANDOM};
	
	/**
	 * Checks was killed by the given dragon.
	 * 
	 * @param p current player
	 * @param d dragon
	 * @return true if player died
	 */
	public static boolean deathCondition(Player p, Dragon d){
		if(((p.getX() == d.getX() && Math.abs(p.getY() - d.getY())<= 1 ) || (p.getY() == d.getY() && Math.abs(p.getX() - d.getX())<= 1 )) && d.getDraw() ) //adjacentes
		{
			if(p.isArmed())
				{
					d.setDraw(false);
					return false;
				}
			else
				if(d.getAsleep())
					return false;
				p.setDraw(false);
				return true;
		}
		return false;
	}
	/**
	 * check if game has been won
	 * @param g gamestate to be checked
	 * @return true if game is won
	 */
	public static boolean winCondition(GameState g){
		return g.getLabyrithm().getPlayerTile(g.getPlayer()).getType().equals(Tile.TileType.EXIT);
	}

	/**
	 * resolve one turn of the game given player input
	 * 
	 * @param g current gamestate
	 * @param input player input
	 * @return true if game ended
	 */
	public static boolean gameLoop(GameState g, GameLogic.Movement input){
		for(int i = 0; i < g.getProjectiles().size(); i++){
			if(g.getProjectiles().get(i).collision(g))
				{i--;continue;}
			g.getProjectiles().get(i).move();
		}
		if(!g.getPlayer().getDraw())
			return true;
		Player p = g.getPlayer();
		if(input.equals( GameLogic.Movement.UP))
			p.move(-1, 0, g);
		else if(input.equals( GameLogic.Movement.LEFT))
			p.move(0, -1, g);
		else if(input.equals( GameLogic.Movement.DOWN))
			p.move(1, 0, g);
		else if(input.equals( GameLogic.Movement.RIGHT))
			p.move(0, 1, g);
		else if(input.equals( GameLogic.Movement.THROW_UP))
			p.throwDart(-1, 0, g);
		else if(input.equals( GameLogic.Movement.THROW_LEFT))
			p.throwDart(0, -1, g);
		else if(input.equals( GameLogic.Movement.THROW_DOWN))
			p.throwDart(1, 0, g);
		else if(input.equals( GameLogic.Movement.THROW_RIGHT))
			p.throwDart(0, 1, g);
		ArrayList<Dragon> dragons = g.getDragons();
		for(Dragon di : dragons){
			di.move(g);
			if(GameLogic.deathCondition(p, di) || !g.getPlayer().getDraw())
				return true;
		}
		return false;
	}
}
