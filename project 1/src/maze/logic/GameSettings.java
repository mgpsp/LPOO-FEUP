package maze.logic;

import java.io.Serializable;

import maze.logic.Dragon.DragonBehaviour;

/**
 * Game settings.
 */
public class GameSettings  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1179988726033287475L;
	public final static int MIN_SIZE = 7;
	private int numdragons;
	private int numquivers;
	private int mazeHeight;
	private int mazeWidth;
	private DragonBehaviour dragonBehaviour;
	
	/**
	 * copy settings from another GameSettings object
	 * @param g GameSettings to copy
	 */
	public void setAttributes(GameSettings g){
		this.numdragons = g.getNumdragons();
		this.numquivers= g.getNumquivers();
		this.mazeHeight = g.getMazeHeight();
		this.mazeWidth = g.getMazeWidth();
		this.dragonBehaviour= g.getDragonBehaviour();
	}
	/**
	 * default Constructor for class GameSettings.
	 */
	public GameSettings(){
		this(1,1,15,15,Dragon.DragonBehaviour.SLEEPING);
	}
	
	/**
	 * Constructor for class GameSettings.
	 * 
	 * @param numdragons number of dragons
	 * @param numquivers number of quivers
	 * @param mazeHeight maze height
	 * @param mazeWidth maze width
	 * @param dragonBehaviour dragon's behaviour
	 */
	public GameSettings(int numdragons,	int numquivers, int mazeHeight, int mazeWidth, DragonBehaviour dragonBehaviour){
		if(numdragons < 1 || numdragons > maxDragons(mazeHeight) || mazeHeight < MIN_SIZE || mazeWidth < MIN_SIZE)
			throw new IllegalArgumentException();
		this.numdragons = numdragons;
		this.numquivers= numquivers;
		this.mazeHeight = mazeHeight;
		this.mazeWidth = mazeWidth;
		this.dragonBehaviour = dragonBehaviour;
	}
	
	/**
	 * Gets the number of dragons.
	 * 
	 * @return number of dragons
	 */
	public int getNumdragons() {
		return numdragons;
	}
	
	/**
	 * Sets the number of dragons.
	 * 
	 * @param numdragons number of dragons to set
	 */
	public void setNumdragons(int numdragons) {
		this.numdragons = numdragons;
	}
	
	/**
	 * Gets number of quivers.
	 * 
	 * @return returns the number of quivers
	 */
	public int getNumquivers() {
		return numquivers;
	}
	
	/**
	 * Sets the number of quivers.
	 * 
	 * @param numquivers number of quivers to set
	 */
	public void setNumquivers(int numquivers) {
		this.numquivers = numquivers;
	}
	
	/**
	 * Gets the maze height.
	 * 
	 * @return returns the maze height
	 */
	public int getMazeHeight() {
		return mazeHeight;
	}
	
	/**
	 * Sets the maze height.
	 * 
	 * @param mazeHeight maze height to set
	 */
	public void setMazeHeight(int mazeHeight) {
		this.mazeHeight = mazeHeight;
	}
	
	/**
	 * Gets the maze width.
	 * 
	 * @return returns the maze width
	 */
	public int getMazeWidth() {
		return mazeWidth;
	}
	
	/**
	 * Sets the maze width.
	 * 
	 * @param mazeWidth maze width to set
	 */
	public void setMazeWidth(int mazeWidth) {
		this.mazeWidth = mazeWidth;
	}
	
	/**
	 * Gets the dragons behaviour.
	 * 
	 * @return returns the dragons behaviour
	 */
	public DragonBehaviour getDragonBehaviour() {
		return dragonBehaviour;
	}
	
	/**
	 * Sets the dragon behaviour.
	 * 
	 * @param dragonBehaviour behaviour to set
	 */
	public void setDragonBehaviour(DragonBehaviour dragonBehaviour) {
		this.dragonBehaviour = dragonBehaviour;
	}
	
	/**
	 * Gets the maximum numbetr of dragons that can be in a maze.
	 * 
	 * @param size size of the maze
	 * @return returns the maximum number of dragons
	 */
	public static int maxDragons(int size){
		return Math.max(size/2,1);
	}
	
}
