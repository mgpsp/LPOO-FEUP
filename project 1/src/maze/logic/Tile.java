package maze.logic;

import java.io.Serializable;

/**
 *
 */
public class Tile  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6968882965798325294L;
	public enum TileType{
		FLOOR, WALL, EXIT,
	}
	/**
	 * constructor for tile
	 * @param type type of tile
	 */
	public Tile(TileType type){
		this.type = type;
	}
	
	/**
	 * default constructor for class
	 */
	public Tile(){
		this(TileType.FLOOR);
	}
	
	/**
	 * get the type of tile
	 * @return type of tile
	 */
	public TileType getType() {
		return type;
	}

	/**
	 * set the type of tile
	 * @param type type of tile to set
	 */
	public void setType(TileType type) {
		this.type = type;
	}
	private TileType type;
}
