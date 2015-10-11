package maze.logic;
/**
 * 
 */
import java.util.*;

/**
 *
 */
public class MazeGen {
		
		private static final int MAX_ATTEMPTS = 1000;
		
		private static ArrayList<Pair<Integer, Integer>> findAdjacents(int size, int step, Pair<Integer, Integer> current){
			ArrayList<Pair<Integer, Integer>> out = new ArrayList<Pair<Integer, Integer>>();
			int x, y;
			for(int i=0; i <= 1;i++)
				for(int j = 0; j<= 1; j++)
				{
					x= current.first;
					y = current.second;
					if(i==0)
					{
						if(j==0)
						x -=step;
						else x+=step;
					}
					else{
						if(j==0)
						y -= step;
						else y +=step;
					}
					if(!(x < 0 || y < 0 || x >= size|| y >= size))
						out.add(new Pair<Integer, Integer>(x,y));
				}
			return out;
		}
		
		private static  ArrayList<Pair<Integer, Integer>> findUnvisitedAdjacents(ArrayList<Pair<Integer, Integer>> adjacents, ArrayList<Pair<Integer, Integer>> unvisited ){
			ArrayList<Pair<Integer, Integer>> out = new ArrayList<Pair<Integer, Integer>>();
			for (Pair<Integer, Integer> t : adjacents) {
	            if(unvisited.contains(t)) {
	                out.add(t);
	            }
	        }
			return out;
		}
		
		private static Pair<Integer, Integer> getRandomPosition(ArrayList<Pair<Integer, Integer>> path){
			if(path.isEmpty())
				return null;
			Random r = new Random();
			int index = r.nextInt(path.size());
			Pair<Integer, Integer> out = path.get(index);
			path.remove(index);
			for(int i = 0; i <  path.size(); i++){
				if(path.get(i).first == out.first && path.get(i).second == out.second  ){
					path.remove(i);
					i--;
				}
			}
			removeAdjacents(path, out);
			return out;
		}
		
		private static void removeAdjacents(ArrayList<Pair<Integer, Integer>> path, Pair<Integer, Integer> cell){
			for(int i = 0; i < path.size();i++){
				if((path.get(i).first == cell.first && Math.abs(path.get(i).second-cell.second) <= 1)||( path.get(i).second == cell.second && Math.abs(path.get(i).first-cell.first) <= 1 )){
					path.remove(i);
					i--;
				}
			}
			return;
		}
		
		@SuppressWarnings("unchecked")
		private static boolean genPos(GameState state, int exit_x, int exit_y, ArrayList<Pair<Integer, Integer>> path){
			int attempts = 0;

			ArrayList<Pair<Integer, Integer>> pathclone = (ArrayList<Pair<Integer, Integer>>) path.clone();
			while (true)
			{
				if(attempts > MAX_ATTEMPTS)
					return false;
				attempts++;
				if(attempts != 1)
					path = (ArrayList<Pair<Integer, Integer>>) pathclone.clone();
				Pair<Integer, Integer> player_position = getRandomPosition(path);
				if(player_position == null)
					continue;

				Pair<Integer, Integer> sword_position = getRandomPosition(path);
				if(sword_position == null)
					continue;
				Pair<Integer, Integer> shield_position = getRandomPosition(path);
				if(shield_position == null)
					continue;
				Pair<Integer, Integer> dragon_position = null;
				ArrayList<Pair<Integer, Integer>> dragon_positions = new ArrayList<Pair<Integer, Integer>>();
				for(int i = 0 ; i < state.getSettings().getNumdragons(); i++){
					dragon_position = getRandomPosition(path);
					if(dragon_position == null)
						break;
					dragon_positions.add(dragon_position);
				}
				if(dragon_position == null)
					continue;
				Pair<Integer, Integer> quiver_position = null;
				ArrayList<Pair<Integer, Integer>> quiver_positions = new ArrayList<Pair<Integer, Integer>>();
				for(int i = 0 ; i < state.getSettings().getNumquivers(); i++){
					quiver_position = getRandomPosition(path);
					if(quiver_position == null)
						break;
					quiver_positions.add(quiver_position);
				}
				if(quiver_position == null && state.getSettings().getNumquivers() != 0)
					continue;
				
				state.addItem(new Sword(sword_position.first,sword_position.second));
				state.addItem(new Shield(shield_position.first,shield_position.second));
				//state.getPlayer().setX(player_position.first);state.getPlayer().setY(player_position.second);
				state.setPlayer(new Player(player_position.first,player_position.second));
				for(Pair<Integer, Integer> i : dragon_positions){
					state.addDragon(new Dragon(i.first,i.second));
				}
				for(Pair<Integer, Integer> i : quiver_positions){
					state.addItem(new DartQuiver(i.first,i.second, 1));
				}
				return true;
			}
		}
		
		/**
		 * generate a maze according to a state's settings
		 * @param state state containing maze to generate
		 */
		public static void gen(GameState state){
			Dragon.setBehaviour(state.getSettings().getDragonBehaviour());
			int size = state.getSettings().getMazeHeight();
			if( size % 2 == 0)
				size++;
			state.setLabyrithm(new Labyrithm(size, size));
			state.setDragons(new ArrayList<Dragon>());
			state.setItems(new ArrayList<Item>());
			state.setProjectiles(new ArrayList<Projectile>());
			
			ArrayList<Pair<Integer,Integer>> unvisited=new ArrayList<Pair<Integer,Integer>>(); 
			for(int i=0; i < size; i++)
				for(int j =0; j < size; j++ )
				{
					if(i % 2 == 0 || j % 2 == 0)
						{
							state.getLabyrithm().setTile(i,j,Tile.TileType.WALL);
						}
					else unvisited.add(new Pair<Integer, Integer>(i,j));
				}
			Random r = new Random();
			int initial_x, initial_y;
			int exit_x, exit_y;
			if(r.nextBoolean())	{// y ->extreme 
				if(r.nextBoolean()){
					exit_y=0;
					initial_y = exit_y + 1;
				}
				else{
					exit_y = size - 1;
					initial_y = exit_y - 1;
				}
				exit_x = r.nextInt((size-1)/2)*2+1;
				initial_x = exit_x;
			}
			else{
				if(r.nextBoolean()){
					exit_x=0;
					initial_x = exit_x + 1;
				}
				else {
					exit_x = size - 1;
					initial_x = exit_x - 1;
				}
				exit_y =r.nextInt((size-1)/2)*2+1;
				initial_y = exit_y;
			}
			state.getLabyrithm().setTile(exit_x,exit_y,Tile.TileType.EXIT);
			Pair<Integer, Integer> current = new Pair<Integer, Integer>(initial_x,initial_y);	
			Pair<Integer, Integer> next = null;
			ArrayList<Pair<Integer, Integer>> path = new ArrayList<Pair<Integer, Integer>>();
			Stack<Pair<Integer, Integer>> previous = new Stack<Pair<Integer, Integer>>();
			ArrayList<Pair<Integer, Integer>> unvisitedadjacents = null;
			
			while(!unvisited.isEmpty())
			{
				path.add(current);
				previous.push(current);
				
				unvisitedadjacents = findUnvisitedAdjacents(findAdjacents(size, 2, current), unvisited);
				while(unvisitedadjacents.isEmpty())
				{
					previous.pop();
					current = previous.peek();
					unvisitedadjacents = findUnvisitedAdjacents(findAdjacents(size, 2, current), unvisited);
				}
				next = unvisitedadjacents.get(r.nextInt(unvisitedadjacents.size()));
				state.getLabyrithm().setTile(current.first-(current.first-next.first)/2,current.second-(current.second-next.second)/2,Tile.TileType.FLOOR);
				path.add(new Pair<Integer, Integer>(current.first-(current.first-next.first)/2,current.second-(current.second-next.second)/2));
				current = next;
				unvisited.remove(current);
			}
			//adjust walls
			int adjusted = 0;
			int adjustmentIterations = size*size;
			int maxAdjustments = size;
			for(int i = 0; i < adjustmentIterations && adjusted < maxAdjustments; i++){
				int x1, y1;
				do{
					x1 = r.nextInt(size-2)+1;
					y1 = r.nextInt(size-2)+1;
				}while(x1 == y1);
				boolean left = state.getLabyrithm().getTileType(x1-1, y1) == Tile.TileType.WALL;
				boolean right=	state.getLabyrithm().getTileType(x1+1, y1) == Tile.TileType.WALL;
				boolean down = state.getLabyrithm().getTileType(x1, y1-1) == Tile.TileType.WALL;
				boolean up = state.getLabyrithm().getTileType(x1, y1+1) == Tile.TileType.WALL;
				boolean middle = state.getLabyrithm().getTileType(x1, y1) == Tile.TileType.WALL;
				if(middle && (left&& right || up && down) && !((up||down)&&(left||right)))
				{
					state.getLabyrithm().setTile(x1, y1, Tile.TileType.FLOOR);
					path.add(new Pair<Integer, Integer>(x1,y1));
					adjusted++;
				}
					
			}
			genPos(state, exit_x,exit_y, path);
			
		}
		
		/**
		 * generate an empty maze with a given size
		 * @param g state containing maze to generate
		 * @param size size of maze to generate
		 */
		static void genEmpty(GameState g, int size){
			g.setLabyrithm(new Labyrithm(size, size));
			for(int i=0; i < size; i++)
				for(int j =0; j < size; j++ )
				{
					if(i == 0 || j  == 0 || i == size-1 || j == size-1)
					{
						g.getLabyrithm().setTile(i,j,Tile.TileType.WALL);
					}
				}
		}
}
