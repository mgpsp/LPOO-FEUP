package maze.cli;

import java.util.*;

import maze.logic.Dart;
import maze.logic.DartQuiver;
import maze.logic.Entity;
import maze.logic.Fireball;
import maze.logic.GameLogic;
import maze.logic.GameSettings;
import maze.logic.GameState;
import maze.logic.Labyrithm;
import maze.logic.Dragon;
import maze.logic.Player;
import maze.logic.Dragon.DragonBehaviour;
import maze.logic.Shield;
import maze.logic.Tile;
import maze.logic.Sword;

public class Cli {

	private static Scanner user_input = new Scanner( System.in );
	public static GameState setup(){
		GameState out= new GameState();
		int response = 0;

		while(true){
			System.out.print("'1' for template maze, '2' for random:");
			try{
				response = user_input.nextInt();
			}catch(Exception e){user_input.nextLine();}
			if(response == 1 || response == 2)
				break;
		}
		if(response == 1){

			out.setLabyrithm(new Labyrithm(Labyrithm.template1));
			out.setPlayer (new Player(1,1));
			out.addDragon (new Dragon(1,3));
			out.addItem(new Sword(1,8));
			System.out.println();
		}
		else {
			while(true){
				System.out.print("Insert size of maze:");
				int size=0;
				try{
					size = user_input.nextInt();
				}catch(Exception e){user_input.nextLine();}
				if(size < 5)
					continue;
				System.out.print("Insert number of dragons:");
				try{
					response = user_input.nextInt();
				}catch(Exception e){user_input.nextLine();continue;}
				if(response < 1 || response >  GameSettings.maxDragons(size)){
					System.out.println("Invalid number of dragons, must be between 1 and " + Math.max(size/4,1));
					continue;
				}
				try{
					out = new GameState(size, response);
				} catch(Exception e){continue;}
				break;
			}
			while(true)
			{
				System.out.println("Stationary [0]: ");
				System.out.println("Moving     [1]: ");
				System.out.println("Sleeping   [2]: ");
				System.out.print("Insert behaviour of dragons:");
				try{
					response = user_input.nextInt();
				}catch(Exception e){user_input.nextLine();}
				if(response == 0)
				{
					Dragon.setBehaviour(DragonBehaviour.STATIONARY);
					break;
				}
				else if(response == 1){
					Dragon.setBehaviour(DragonBehaviour.MOVING);
					break;
				}
				else if(response == 2){
					Dragon.setBehaviour(DragonBehaviour.SLEEPING);
					break;
				}
			}


		}
		return out;
	}
	public static GameLogic.Movement input(GameState g){
		String input;
		input = user_input.next();
		if(input.length() == 0)
			return GameLogic.Movement.NONE;
		char comp = Character.toUpperCase(input.charAt(0));		
		if(comp == 'W')
			return GameLogic.Movement.UP;
		if(comp == 'A')
			return GameLogic.Movement.LEFT;
		if(comp == 'S')
			return GameLogic.Movement.DOWN;
		if(comp == 'D')
			return GameLogic.Movement.RIGHT;
		if(comp == 'T')
			do
			{
				input = user_input.next();
				comp = Character.toUpperCase(input.charAt(0));
				if(comp == 'W')
					return GameLogic.Movement.THROW_UP;
				if(comp == 'A')
					return GameLogic.Movement.THROW_LEFT;
				if(comp == 'S')
					return GameLogic.Movement.THROW_DOWN;
				if(comp == 'D')
					return GameLogic.Movement.THROW_RIGHT;
				if(comp == 'T')
					return GameLogic.Movement.NONE;
			}while(true);

		return GameLogic.Movement.NONE;
	}
	public static void printGame(GameState g){
		//g.getLabyrithm().print(g.getPlayer(),g.getDragon());
		int lin = g.getLabyrithm().getWidth();
		int col = g.getLabyrithm().getHeight();
		PriorityQueue<Entity> ents = getPrintQueue(g);
		int dart = g.getPlayer().getNumDarts();
		for(int i = 0; i < lin; i++)
		{
			for(int j = 0; j < col; j++)
			{
				boolean drawn = false;
				while(!ents.isEmpty() && i == ents.peek().getX() && j == ents.peek().getY())
				{
					if(!drawn){
						if(ents.peek().getDraw() || ents.peek() instanceof Player){
							System.out.print(getChar(ents.peek()));
							drawn = true;
						}
					}ents.remove();
				}
				if(!drawn) 
					System.out.print(Labyrithm.getCharFromTileType(g.getLabyrithm().getTileType(i,j)));
				//System.out.print(g.getLabyrithm().getTile(i, j).getChar());
				System.out.print(" ");

			}
			if(dart > 0){
				System.out.print(" |");
				dart--;
			} else System.out.print("  ");
			if(i == 0 && g.getPlayer().isShielded())
				System.out.print(" O");
			System.out.println();
		}
	}
	public static void closeInput(){
		user_input.close();
	}
	public static void printEnd(boolean win){
		if(win){
			System.out.print("YAY!!");
		}else {
			System.out.print("NO!!!");
		}
	}
	public static PriorityQueue<Entity> getPrintQueue(GameState g){
		Comparator<Entity> comparator = new Entity.EntityPositionComparator();
		PriorityQueue<Entity> out = new PriorityQueue<Entity>(g.getDragons().size()+g.getItems().size()+g.getProjectiles().size()+1,comparator);
		if(g.getPlayer() != null)
			out.add(g.getPlayer());
		out.addAll(g.getDragons());
		out.addAll(g.getItems());
		out.addAll(g.getProjectiles());
		return out;
	}
	public static char getChar(Entity e){
		if(e instanceof Player){
			if(((Player) e).isArmed())
				return 'A';
			else return 'H';
		}
		if(e instanceof Dragon){
			if(((Dragon) e).getAsleep())
				return 'd';
			else return 'D';
		}
		if(e instanceof Sword)
			return 'E';
		if(e instanceof Shield)
			return 'O';
		if(e instanceof DartQuiver)
			return 'Q';
		if(e instanceof Fireball)
			return '*';
		if(e instanceof Dart){
			if(((Dart) e).getHorizontal_movement())
				return '|';
			else return '-';
		}
		return ' ';
	}
	public static char[][] getMatrix(Labyrithm l){
		char[][] out = new char[l.getHeight()][l.getWidth()];
		for(int i = 0; i < l.getHeight(); i++){
			for(int j = 0; j < l.getWidth(); j++){
				out[i][j] = getChar(l.getTile(i, j));
			}
		}
		return out;
	}
	public static char getChar(Tile tile){
		return Labyrithm.getCharFromTileType(tile.getType());
	}

}

