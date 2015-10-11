package maze.logic;

import java.util.*;
import java.io.Serializable;
import maze.logic.Dragon.DragonBehaviour;


/**
 *
 */
public class GameState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5592434751098027717L;
	private static final int MIN_SIZE = 7;
	private ArrayList<Dragon> dragons= new ArrayList<Dragon>();
	private ArrayList<Item> items = new ArrayList<Item>();
	private  Labyrithm labyrithm = null;
	private  Player player;
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	private GameSettings settings;
	
	/**
	 * copy all data from another gamestate
	 * @param g2 gamestate to copy
	 */
	public void copy(GameState g2){
		dragons = g2.dragons;
		items = g2.items;
		labyrithm = g2.labyrithm;
		player = g2.player;
		projectiles = g2.projectiles;
		settings = g2.settings;
	}
	
	/**
	 * default constructor
	 */
	public GameState(){
	}
	
	/**
	 * generate an empty maze of a given size
	 * @param size size of empty maze to generate
	 */
	public GameState(int size){
		if(size < MIN_SIZE)
			throw new IllegalArgumentException("illegal maze size");
		MazeGen.genEmpty(this,size);
	}
	
	/**
	 * Generate new maze with given size and number of dragons
	 * @param size size of maze to generate
	 * @param numdragons number of dragons to put in maze
	 */
	public GameState(int size, int numdragons){
		if(size < MIN_SIZE)
			throw new IllegalArgumentException("illegal maze size");
		settings = new GameSettings(numdragons, numdragons, size, size, DragonBehaviour.SLEEPING);
		MazeGen.gen(this);
	}
	
	/**
	 * regenerate the maze according to its settings
	 */
	public void regen(){
		if(settings == null)
			settings = new GameSettings();
		MazeGen.gen(this);
	}
	
	/**
	 * generate a maze according to the attributes of a GameSettings object
	 * @param settings new settings to generate from
	 */
	public GameState(GameSettings settings){
		this.settings = settings;	
		MazeGen.gen(this);
	}
	
	/**
	 * create a GameSettings object according to current state of game
	 */
	public void restoreSettings(){
		this.settings = new GameSettings(dragons.size(), getNumQuivers(), labyrithm.getHeight(), labyrithm.getWidth(), Dragon.getBehaviour());
	}
	
	/**
	 *  add a dragon to the game
	 * @param d dragon to add
	 */
	public void addDragon(Dragon d){
		dragons.add(d);
	}
	
	/**
	 * add an item to the game
	 * @param item item to add
	 */
	public void addItem(Item item) {
		items.add(item);
	}
	
	/**
	 * get all the dragons in the game 
	 * @return dragons in game
	 */
	public ArrayList<Dragon> getDragons(){
		return dragons;
	}
	
	/**
	 * get all the items in the game
	 * @return items in the game
	 */
	public ArrayList<Item> getItems() {
		return items;
	}
	
	/**
	 * get the maze
	 * @return current maze
	 */
	public Labyrithm getLabyrithm() {
		return labyrithm;
	}
	
	/**
	 * get the player entity
	 * @return player entity
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * get all the projectiles in the game
	 * @return projectiles in gamestate
	 */
	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}
	

	/**
	 * set the labyrithm in the state
	 * @param labyrithm labyrithm to set
	 */
	public void setLabyrithm(Labyrithm labyrithm) {
		this.labyrithm = labyrithm;
	}
	
	/**
	 * set the player entity
	 * @param player player entity to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * add a projectile to the game state
	 * @param projectile projectile to add
	 */
	public void addProjectiles(Projectile projectile) {
		projectiles.add(projectile);
	}
	
	/**
	 * remove projectile from game state 
	 * @param projectile projetile to remove
	 * @return success of operation
	 */
	public boolean removeProjectile(Projectile projectile){
		return projectiles.remove(projectile);
	}

	/**
	 * get number of dragons that are alive in game
	 * @return number of live dragons
	 */
	public int getLiveDragons() {
		int count = 0;
		for(Dragon i : dragons){
			if(i.getDraw())
				count++;
		}
		return count;
	}
	
	/**
	 * get the position of player entity
	 * @return position of player, null if there is none
	 */
	public Pair<Integer, Integer> getHeroPosition() {
		if(player == null)
			return null;
		return new Pair<Integer, Integer>(player.getX(), player.getY());
	}
	
	/**
	 * get the position of the first dragon to be added
	 * @return position of dragon
	 */
	public Pair<Integer, Integer> getDragonPosition() {
		if(dragons.size() == 0 || dragons.get(0) == null)
			return null;
		return new Pair<Integer, Integer>(dragons.get(0).getX(), dragons.get(0).getY());
	}
	
	/**
	 * get position of first sword to be added
	 * @return position of sword
	 */
	public Pair<Integer, Integer> getSpadePosition() {
		for(Item i : items){
			if(i  != null && i instanceof Sword){
				return new Pair<Integer, Integer>(i.getX(), i.getY());
			}
		}
		return null;
	}
	
	/**
	 * set all dragons in game state
	 * @param arrayList dragons to set
	 */
	public void setDragons(ArrayList<Dragon> arrayList) {
		dragons = arrayList;
		
	}
	
	/**
	 * set items in game state
	 * @param arrayList items to set
	 */
	public void setItems(ArrayList<Item> arrayList) {
		items = arrayList;
		
	}
	
	/**
	 * set projectiles in game state
	 * @param arrayList items to set
	 */
	public void setProjectiles(ArrayList<Projectile> arrayList) {
		projectiles = arrayList;
	}
	
	/**
	 * get the game's settings
	 * @return settings
	 */
	public GameSettings getSettings(){
		return settings;
	}
	
	/**
	 * get the number of quivers in state
	 * @return number of quivers in state
	 */
	public int getNumQuivers(){
		int count = 0;
		for(Item i  : items )
			if(i instanceof DartQuiver)
				count++;
		return count;
	}
	
	/**
	 * get all entities in game state
	 * @return all entities
	 */
	public ArrayList<Entity> getAllEntities(){
		ArrayList<Entity> out = new ArrayList<Entity>();
		out.add(player);
		out.addAll(dragons);
		out.addAll(items);
		out.addAll(projectiles);
		return out;
	}
	
	/**
	 * determine what entity is on a tile
	 * @param x x position of tile to check
	 * @param y y position of tile to check
	 * @return entity standing on tile
	 */
	public Entity tileOccupied(int x, int y) {
		if(labyrithm.getTileType(x,y) != Tile.TileType.FLOOR)
			return null;
		for(Entity e  : getAllEntities()){
			if(e != null && (e.getX() == x && e.getY() == y))
				return e;
		}
		return null;
	}
	
	/**
	 * remove dragon from game state
	 * @param d dragon to remove
	 */
	public void removeDragon(Dragon d){
		dragons.remove(d);
	}
	
	/**
	 * remove item from game state
	 * @param d item to remove
	 */
	public void removeItem(Item d){
		items.remove(d);
	}
	/**
	 * set the games settings
	 * @param gameSettings settings to set
	 */
	public void setSettings(GameSettings gameSettings) {
		this.settings = gameSettings;
	}

}
