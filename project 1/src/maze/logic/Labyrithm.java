package maze.logic;
/**
 * 
 */
import java.io.Serializable;
import java.util.*;

import maze.logic.Tile;
/**
 * @author up201304000
 *
 */
/**
 *
 */
public class Labyrithm  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -415950319912780812L;
	public static char[][] template1 = 
		{
			{'X','X','X','X','X','X','X','X','X','X'},
			{'X',' ',' ',' ',' ',' ',' ',' ',' ','X'},
			{'X',' ','X','X',' ','X',' ','X',' ','X'},
			{'X',' ','X','X',' ','X',' ','X',' ','X'},
			{'X',' ','X','X',' ','X',' ','X',' ','X'},
			{'X',' ',' ',' ',' ',' ',' ','X',' ','S'},
			{'X',' ','X','X',' ','X',' ','X',' ','X'},
			{'X',' ','X','X',' ','X',' ','X',' ','X'},
			{'X',' ','X','X',' ',' ',' ',' ',' ','X'},
			{'X','X','X','X','X','X','X','X','X','X'},
		};
	
	/**
	 * constructor for empty maze
	 * @param linsize size of maze in lines
	 * @param colsize size of maze in columns
	 */
	Labyrithm(int linsize, int colsize){
		maze = new ArrayList<ArrayList<Tile>>();
		for(int i = 0; i < linsize; i++) {
			ArrayList<Tile> temp = new ArrayList<Tile>();
			for(int j = 0; j < linsize; j++)
			{
				temp.add(new Tile());
			}
			maze.add(temp);
		}
	}
	
	/**
	 * create a maze according to a given template
	 * @param template matrix containing information of maze
	 */
	public Labyrithm(char[][] template)
	{
		this(template.length, template[0].length);
		fill(template);
	}
	
	/**
	 * fill a maze according to a given template
	 * @param template to fill
	 */
	public void fill(char[][] template){
		int lin = template.length;
		int col = template[0].length;
		
		for(int i = 0; i < lin; i++)
			for(int j = 0; j < col; j++)
				setTile(i, j, getTileTypeFromChar(template[i][j]));
	}
	
	/**
	 * get the width of the maze
	 * @return width of maze
	 */
	public int getWidth(){
		if(maze.size() == 0)
			return 0;
		return maze.get(0).size();
	}
	
	/**
	 * get the height of the maze
	 * @return height of maze
	 */
	public int getHeight(){
		return maze.size();
	}
	
	/**
	 * get the tile in a position
	 * @param x x position of tile
	 * @param y y position of tile
	 * @return tile in x y position
	 */
	public Tile getTile(int x, int y){
		return maze.get(x).get(y);
	}
	
	/**
	 * get type of tile in x y position
	 * @param x x position of tile
	 * @param y y position of tile
	 * @return type of tile in x y position
	 */
	public Tile.TileType getTileType(int x, int y){
		return maze.get(x).get(y).getType();
	}
	
	/**
	 * get the tile player is standing on
	 * @param p player
	 * @return tile player is standing on
	 */
	public Tile getPlayerTile(Player p){
		return getTile(p.getX(), p.getY());
	}
	
	/**
	 * set type of tile 
	 * @param x x position of tile to set
	 * @param y y position of tile to set
	 * @param newTile type of tile to set
	 * @return true if operation successful
	 */
	public boolean setTile(int x, int y, Tile.TileType newTile){
		if(x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
			return false;
		maze.get(x).get(y).setType(newTile);
		return true;
	}
	
	/**
	 * get the position of the exit
	 * @return position of exit, null if thereis none
	 */
	public Pair<Integer, Integer> getExitPosition(){
		for(int i = 0; i < getHeight(); i++){
			for(int j = 0; j < getWidth(); j++){
				if(getTileType(i,j).equals(Tile.TileType.EXIT))
					return new Pair<Integer, Integer>(i, j);
			}
		}
		return null;
	}
	/**
	 * get the character associated with a type of tile
	 * @param tile type of tile
	 * @return character associated with type of tile
	 */
	public static char getCharFromTileType(Tile.TileType tile){
		if(tile.equals(Tile.TileType.FLOOR))
			return '\0';
		if(tile.equals(Tile.TileType.WALL))
			return 'X';
		if(tile.equals(Tile.TileType.EXIT))
			return 'S';
		else return '\0';
	}
	
	/**
	 * get the type of tile associated with a character 
	 * @param c character
	 * @return type of tile associated
	 */
	public static Tile.TileType getTileTypeFromChar(char c){
		if(c == '\0' || c == ' ')
			return Tile.TileType.FLOOR;
		if(c == 'X')
			return Tile.TileType.WALL;
		if(c == 'S')
			return Tile.TileType.EXIT;
		else return Tile.TileType.FLOOR;
	}
	
	
	private ArrayList<ArrayList<Tile>> maze;
}
