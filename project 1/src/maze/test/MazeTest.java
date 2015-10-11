package maze.test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.Test;

import maze.cli.Cli;
import maze.logic.*;
import maze.logic.Dragon.DragonBehaviour;
import junit.framework.TestCase;

public class MazeTest extends TestCase{
	private static ArrayList<GameLogic.Movement> charsToCommands(char[] com){
		ArrayList<GameLogic.Movement> out = new ArrayList<GameLogic.Movement>();
		for(int i =0; i < com.length; i++){
			char comp = Character.toUpperCase(com[i]);		
			if(comp == 'W')
				out.add( GameLogic.Movement.UP);
			if(comp == 'A')
				out.add( GameLogic.Movement.LEFT);
			if(comp == 'S')
				out.add( GameLogic.Movement.DOWN);
			if(comp == 'D')
				out.add( GameLogic.Movement.RIGHT);
			else if(comp == 'R')
				out.add( GameLogic.Movement.RANDOM);
			if(comp == 'T')
				{
					if(i == com.length -1){
						out.add( GameLogic.Movement.NONE);
						return out;
					}
					i++;
					comp = Character.toUpperCase(com[i]);
					if(comp == 'W')
						out.add( GameLogic.Movement.THROW_UP);
					if(comp == 'A')
						out.add( GameLogic.Movement.THROW_LEFT);
					if(comp == 'S')
						out.add( GameLogic.Movement.THROW_DOWN);
					if(comp == 'D')
						out.add( GameLogic.Movement.THROW_RIGHT);
					if(comp == 'T')
						out.add( GameLogic.Movement.NONE);
				}
		}
		return out;
	}
	private static void pad(ArrayList<GameLogic.Movement> p_com, ArrayList<ArrayList<GameLogic.Movement>> dragon_com ){
		int numturns = p_com.size();
		for(ArrayList<GameLogic.Movement> i : dragon_com){
			if(i.size() > numturns)
				numturns = i.size();
		}
		while(p_com.size() < numturns){p_com.add(GameLogic.Movement.NONE);};
		for(ArrayList<GameLogic.Movement> i : dragon_com){
			while(i.size() < numturns){i.add(GameLogic.Movement.NONE);};
		}
		return;
	}
	
	private static boolean testLoop(GameState g,  char[] p_com_char, char[][] dragon_com_char){
		ArrayList<GameLogic.Movement> p_com = charsToCommands(p_com_char);
		ArrayList<ArrayList<GameLogic.Movement>> dragon_com = new ArrayList<ArrayList<GameLogic.Movement>>();
		for(char[] i : dragon_com_char){
			dragon_com.add(charsToCommands(i));
		}
		while(dragon_com.size() < g.getDragons().size()){
			dragon_com.add(new ArrayList<GameLogic.Movement>());
		}
		pad(p_com,dragon_com);
		for(int index = 0; index < p_com.size(); index++){

			for(int i = 0; i < g.getProjectiles().size(); i++){
				if(g.getProjectiles().get(i).collision(g))
				{i--;continue;}
				g.getProjectiles().get(i).move();
			}
			if(!g.getPlayer().getDraw())
				return true;
			Player p = g.getPlayer();
			GameLogic.Movement input = p_com.get(index);
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
			for(int di = 0; di < dragons.size(); di++){
				input = dragon_com.get(di).get(index);
				if(input.equals(GameLogic.Movement.UP))
					g.getDragons().get(di).directedMove(-1, 0, g);
				else if(input.equals(GameLogic.Movement.LEFT))
					g.getDragons().get(di).directedMove(0, -1, g);
				else if(input.equals(GameLogic.Movement.DOWN))
					g.getDragons().get(di).directedMove(1, 0, g);
				else if(input.equals(GameLogic.Movement.RIGHT))
					g.getDragons().get(di).directedMove(0, 1, g);
				else if(input.equals(GameLogic.Movement.RANDOM))
					g.getDragons().get(di).move(g);
				else g.getDragons().get(di).directedMove(0, 0, g);
				if(GameLogic.deathCondition(p,dragons.get(di)) || !g.getPlayer().getDraw())
					return true;
			}
		}
		return false;
	}
	
	
	public static void test1(){
		GameState g= new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(1,1));
		g.addDragon (new Dragon(1,3));
		g.addItem(new Sword(8,1));
		Dragon.setBehaviour(DragonBehaviour.STATIONARY);
		Player p = g.getPlayer();
		int orx, ory, newx, newy;
		orx = p.getX();
		ory = p.getY();
		testLoop(g,new char[] {'S'}, new char[][] {});
		newx = p.getX();
		newy = p.getY();
		assertTrue(orx+1 == newx && ory == newy);
	}
	public static void test2(){
		GameState g= new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(1,1));
		g.addDragon (new Dragon(1,3));
		g.addItem(new Sword(8,1));
		Dragon.setBehaviour(DragonBehaviour.STATIONARY);
		Player p = g.getPlayer();
		int orx, ory, newx, newy;
		orx = p.getX();
		ory = p.getY();
		testLoop(g,new char[] {'A'},new char[][] {});
		newx = p.getX();
		newy = p.getY();
		assertTrue(orx == newx && ory == newy);
	}
	
	public static void test3(){
		GameState g= new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(1,1));
		g.addDragon (new Dragon(1,3));
		g.addItem(new Sword(8,1));
		Dragon.setBehaviour(DragonBehaviour.STATIONARY);
		Player p = g.getPlayer();
		testLoop(g,new char[] {'S','S','S','S','S','S','S'}, new char[][] {});
		Sword s = (Sword) g.getItems().get(0);
		assertTrue(p.isArmed());
		assertTrue(!s.getDraw());
	}
	public static void test4(){
		GameState g= new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(1,1));
		g.addDragon (new Dragon(1,3));
		g.addItem(new Sword(8,1));
		Dragon.setBehaviour(DragonBehaviour.STATIONARY);
		Player p = g.getPlayer();
		testLoop(g,new char[] {'D'},new char[][] {});
		assertTrue(!p.getDraw());
	}
	public static void test5(){
		GameState g= new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(1,1));
		g.addDragon (new Dragon(1,3));
		g.addItem(new Sword(8,1));
		Dragon.setBehaviour(DragonBehaviour.STATIONARY);
		Player p = g.getPlayer();
		testLoop(g,new char[] {'S','S','S','S','S','S','S'}, new char[][] {});
		Sword s = (Sword) g.getItems().get(0);
		//assertTrue(orx == newx && ory == newy);
		assertTrue(p.isArmed());
		assertTrue(!s.getDraw());
		testLoop(g,new char[] {'W','W','W','W','W','W','W'}, new char[][] {});
		testLoop(g,new char[] {'D'}, new char[][] {});
		assertTrue(p.getDraw());
		assertFalse(g.getDragons().get(0).getDraw());
	}
	public static void test6(){
		GameState g= new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(1,1));
		g.addDragon (new Dragon(1,3));
		g.addItem(new Sword(8,1));
		Dragon.setBehaviour(DragonBehaviour.STATIONARY);
		Player p = g.getPlayer();
		testLoop(g,new char[] {'S','S','S', 'S', 'D','D','D','D','D', 'S','S','S', 'D','D', 'W','W','W', 'D'}, new char[][] {});
		assertTrue(p.getDraw());
		assertFalse(g.getLabyrithm().getTileType(p.getX(), p.getY()).equals( Tile.TileType.EXIT));
	}
	public static void testFireball(){
		GameState g= new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(1,1));
		g.addDragon (new Dragon(1,3));
		g.addItem(new Sword(8,1));
		Dragon.setBehaviour(DragonBehaviour.MOVING);
		Player p = g.getPlayer();
		testLoop(g,new char[] {'W'}, new char[][] {{'D'}});
		assertTrue(p.getDraw());
		assertTrue(g.getDragons().get(0).getDraw());
		assertEquals(g.getProjectiles().size(), 1);
		assertEquals(g.getProjectiles().get(0).getX(), 1);
		assertEquals(g.getProjectiles().get(0).getY(),3);
		assertTrue(g.getProjectiles().get(0) instanceof Fireball);
		testLoop(g,new char[] {'W', 'W', 'S'}, new char[][] {{'D','D'}});
		assertEquals(g.getProjectiles().size(),0);
		assertFalse(p.getDraw());
	}
	public static void testFireballs(){
		GameState g= new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(5,4));
		
		g.addDragon (new Dragon(3,4));
		g.addDragon (new Dragon(7,4));
		g.addDragon (new Dragon(5,2));
		g.addDragon (new Dragon(5,6));
		g.addItem(new Sword(8,1));
		Dragon.setBehaviour(DragonBehaviour.MOVING);
		Player p = g.getPlayer();
		testLoop(g,new char[] {'N'}, new char[][] {{'A'},{'D'},{'S'},{'D'}});
		assertTrue(p.getDraw());
		assertEquals(g.getLiveDragons(),4);
		assertEquals(g.getProjectiles().size(), 4);
		//assertEquals(g.getProjectiles().get(0).getX(), 1);
		//assertEquals(g.getProjectiles().get(0).getY(), 3);
		for(Projectile proj : g.getProjectiles()){
			assertTrue(proj instanceof Fireball);
		}
		testLoop(g,new char[] {'N'}, new char[][] {{'A'},{'D'},{'S'},{'D'}});
		testLoop(g,new char[] {'N'}, new char[][] {{'A'},{'D'},{'S'},{'D'}});
		assertFalse(p.getDraw());
		
	}
	public static void testSleep(){
		GameState g= new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(1,1));
		g.addDragon (new Dragon(1,3));
		g.addItem(new Sword(8,1));
		Dragon.setBehaviour(DragonBehaviour.SLEEPING);
		assertEquals(Dragon.getBehaviour(),DragonBehaviour.SLEEPING );
		Player p = g.getPlayer();
		g.getDragons().get(0).sleep(6);
		assertTrue(g.getDragons().get(0).getAsleep());
		assertEquals(Cli.getChar(g.getDragons().get(0)), 'd');
		testLoop(g,new char[] {'D','D','D','D', 'D', 'D'}, new char[][] {{}});
		assertFalse(g.getDragons().get(0).getAsleep());
		assertTrue(p.getDraw());
		assertTrue(g.getDragons().get(0).getDraw());
	}
	public static void testMultiple(){
		GameState g= new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(1,1));
		g.addDragon (new Dragon(1,3));
		g.addDragon(new Dragon(1,5));
		Dragon d = new Dragon(3,5);
		g.addDragon(d);
		g.removeDragon(d);
		g.addItem(new Sword(5,1));
		Dragon.setBehaviour(DragonBehaviour.MOVING);
		Player p = g.getPlayer();
		testLoop(g,new char[] {'S','S','S','S'}, new char[][] {{'D','S','S','S'},{'A', 'A'}});
		assertTrue(g.getDragons().get(0).getDraw());
		assertTrue(g.getDragons().get(1).getDraw());
		assertTrue(g.getDragons().get(0).getX()==4 && g.getDragons().get(0).getY()==4);
		assertTrue(g.getDragons().get(1).getX()==1 && g.getDragons().get(1).getY()==3);
		testLoop(g,new char[] {'D','D','D', 'W','W','W','W','A'}, new char[][] {});
		assertFalse(g.getDragons().get(0).getDraw());
		assertFalse(g.getDragons().get(1).getDraw());
		assertTrue(p.getDraw());
		assertTrue(p.getX()==1&&p.getY()==3);
	}
	public static void testShield(){
		GameState g= new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(1,1));
		g.addDragon (new Dragon(1,4));
		g.addItem(new Shield(2,1));
		Dragon.setBehaviour(DragonBehaviour.MOVING);
		Player p = g.getPlayer();
		assertTrue(g.getItems().get(0).getDraw());
		assertEquals(g.getItems().get(0).getClass(), Shield.class);
		testLoop(g,new char[] {'S'}, new char[][] {});
		assertFalse(g.getItems().get(0).getDraw());
		assertTrue(p.isShielded());
		assertTrue(p.getEquipped().size() == 1);
		testLoop(g,new char[] {'W'}, new char[][] {});
		assertEquals(g.getProjectiles().size(), 1);
		testLoop(g,new char[] {}, new char[][] {{'D', 'D'}});
		assertTrue(p.getX() == g.getProjectiles().get(0).getX() && p.getY() == g.getProjectiles().get(0).getY());
		testLoop(g,new char[] {}, new char[][] {{'D'}});
		assertEquals(g.getProjectiles().size(), 0);
		assertTrue(p.getDraw());
		testLoop(g,new char[] {'D'}, new char[][] {});
		assertTrue(p.getX() ==  1 && p.getY() == 2);
	}
	public static void testDart(){
		GameState g = new GameState();
		g.setLabyrithm(new Labyrithm(Labyrithm.template1));
		g.setPlayer (new Player(1,1));
		assertNotNull(g.tileOccupied(1,1));
		assertNull(g.tileOccupied(0,0));
		assertNull(g.tileOccupied(1,2));
		g.addDragon (new Dragon(1,6));
		assertEquals(g.getNumQuivers(), 0);
		g.addItem(new DartQuiver(2,1,1));
		assertEquals(g.getNumQuivers(), 1);
		Dragon.setBehaviour(DragonBehaviour.MOVING);
		Player p = g.getPlayer();
		assertTrue(g.getItems().get(0).getDraw());
		assertEquals(g.getItems().get(0).getClass(), DartQuiver.class);
		testLoop(g,new char[] {'S'}, new char[][] {});
		assertFalse(g.getItems().get(0).getDraw());
		assertEquals(p.getEquipped().size(), 1);
		assertEquals(p.getNumDarts(), 1);
		testLoop(g,new char[] {'W'}, new char[][] {});
		testLoop(g,new char[] {'T', 'D'}, new char[][] {});
		assertTrue(g.getProjectiles().get(0).getDraw());
		//assertEquals(g.getProjectiles().get(0).getChar(), '-');
		testLoop(g,new char[] {'A', 'A', 'A', 'A'}, new char[][] {});
		assertTrue(g.getDragons().get(0).getX() == g.getProjectiles().get(0).getX() && g.getDragons().get(0).getY()  == g.getProjectiles().get(0).getY());
		testLoop(g,new char[] {'A'}, new char[][] {});
		assertEquals(g.getProjectiles().size(), 0);
		assertFalse(g.getDragons().get(0).getDraw());
	}
	@SafeVarargs
	public final <T> void testAlt(int minIter, 
			Supplier<T> generator, Function<T, String> errorMessage, Predicate<T> ... predicates) {
		boolean [] tested = new boolean[predicates.length];
		int checked = 0;
		for (int iter = 0; iter < minIter && checked < predicates.length; iter++ ) {
			T x = generator.get();
			boolean found = false;
			for (int i = 0; i < predicates.length; i++)
				if (predicates[i].test(x)) {
					found = true;
					if (!tested[i]) {
						checked++;
						tested[i] = true;
					}
				}
			if (! found)		
				fail(errorMessage.apply(x));
			iter++;
		}
	}

	@Test(timeout=1000)
	public void testRandomDragon() {
		testAlt(1000,
				() -> {GameState g = new GameState(); 
						g.setLabyrithm(new Labyrithm(Labyrithm.template1));
						g.setPlayer (new Player(1,1));
						g.addDragon (new Dragon(1,3));
						g.addItem(new Sword(1,8));
						GameLogic.gameLoop(g,GameLogic.Movement.DOWN); 
						return g;},
				(m) -> "Dragon in invalid position: " + m, 
				(m) -> (m.getDragons().get(0).getX() == 1 && m.getDragons().get(0).getY() == 3), 
				(m) -> (m.getDragons().get(0).getX() == 1 && m.getDragons().get(0).getY() == 2), 
				(m) -> (m.getDragons().get(0).getX() == 1 && m.getDragons().get(0).getY() == 4)); 
	}
		// a) the maze boundaries must have exactly one exit and everything else walls
		// b) the exist cannot be a corner
		private boolean checkBoundaries(Labyrithm m) {
			int countExit = 0;
			int n = m.getWidth();
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					if (i == 0 || j == 0 || i == n - 1 || j == n - 1)
						if (Cli.getChar(m.getTile(i, j)) == 'S')
							if ((i == 0 || i == n-1) && (j == 0 || j == n-1))
								return false;
							else
								countExit++;
						else if (Cli.getChar(m.getTile(i, j)) != 'X')
							return false;
			return countExit == 1;
		}
		

		// d) there cannot exist 2x2 (or greater) squares with blanks only 
		// e) there cannot exit 2x2 (or greater) squares with blanks in one diagonal and walls in the other
		// d) there cannot exist 3x3 (or greater) squares with walls only
		private boolean hasSquare(Labyrithm maze, char[][] square) {
			char [][] m = Cli.getMatrix(maze);
			for (int i = 0; i < m.length - square.length; i++)
				for (int j = 0; j < m.length - square.length; j++) {
					boolean match = true;
					for (int x = 0; x < square.length; x++)
						for (int y = 0; y < square.length; y++) {
							if (m[i+x][j+y] != square[x][y])
								match = false;
						}
					if (match)
						return true;
				}		
			return false; 
		}

		// c) there must exist a path between any blank cell and the maze exit 
		private boolean checkExitReachable(Labyrithm maze) {
			Pair<Integer, Integer> p = maze.getExitPosition();
			char [][] m = deepClone(Cli.getMatrix(maze));
			visit(m, p.first, p.second);
			for (int i = 0; i < m.length; i++)
				for (int j = 0; j < m.length; j++)
					if (m[i][j] != 'X' && m[i][j] != 'V')
						return false;

			return true; 
		}

		// auxiliary method used by checkExitReachable
		// marks a cell as visited (V) and proceeds recursively to its neighbors
		private void visit(char[][] m, int i, int j) {
			if (i < 0 || i >= m.length || j < 0 || j >= m.length)
				return;
			if (m[i][j] == 'X' || m[i][j] == 'V')
				return;
			m[i][j] = 'V';
			visit(m, i-1, j);
			visit(m, i+1, j);
			visit(m, i, j-1);
			visit(m, i, j+1);
		}

		// Auxiliary method used by checkExitReachable.
		// Gets a deep clone of a char matrix.
		private char[][] deepClone(char[][] m) {
			char[][] c = m.clone();
			for (int i = 0; i < m.length; i++)
				c[i] = m[i].clone();
			return c;
		}

		// Checks if all the arguments (in the variable arguments list) are not null and distinct
		@SuppressWarnings("unchecked")
		private <T> boolean notNullAndDistinct(T ... args) {
			for (int i = 0; i < args.length - 1; i++)
				for (int j = i + 1; j < args.length ; j++)
					if (args[i] == null || args[j] == null || args[i].equals(args[j]))
						return false;
			return true;
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testRandomMazeGenerator() throws Exception {
			int numMazes = 1000;
			int maxSize = 101; // can change to any odd number >= 5

			char[][] badWalls = {
					{'X', 'X', 'X'},
					{'X', 'X', 'X'},
					{'X', 'X', 'X'}};
			char[][] badSpaces = {
					{' ', ' '},
					{' ', ' '}};
			char[][] badDiag1 = {
					{'X', ' '},
					{' ', 'X'}};
			char[][] badDiag2 = {
					{' ', 'X'},
					{'X', ' '}};
			Random rand = new Random();
			GameState g = new GameState(7);
			GameSettings gs = new GameSettings();
			for (int i = 0; i < numMazes; i++) {
				int size = maxSize == 5? 5 : 5 + 2 * rand.nextInt((maxSize - 5)/2);
				if(size == 5)
					size = 7;
				if(size == g.getLabyrithm().getHeight())
					g.regen();
				else {
					gs.setAttributes(new GameSettings(1,1,size,size,DragonBehaviour.SLEEPING));
					g.copy(new GameState(gs));
				}
	
				Labyrithm m = g.getLabyrithm();
				assertTrue("Invalid maze boundaries in maze:\n" + m, checkBoundaries(m));			
				assertTrue("Maze exit not reachable in maze:\n" + m, checkExitReachable(m));			
				assertNotNull("Invalid walls in maze:\n" + m, ! hasSquare(m, badWalls));
				assertNotNull("Invalid spaces in maze:\n" + m, ! hasSquare(m, badSpaces));
				assertNotNull("Invalid diagonals in maze:\n" + m, ! hasSquare(m, badDiag1));
				assertNotNull("Invalid diagonals in maze:\n" + m, ! hasSquare(m, badDiag2));
				if(!notNullAndDistinct(m.getExitPosition(), g.getHeroPosition(), g.getDragonPosition(), g.getSpadePosition()))
					System.out.println(m.getExitPosition() + "|" + g.getHeroPosition() + "|" + g.getDragonPosition() + "|" + g.getSpadePosition() + "-> size:" + size);
				assertTrue("Missing or overlapping objects in maze:\n" + m, 
						notNullAndDistinct(m.getExitPosition(), g.getHeroPosition(),
								g.getDragonPosition(), g.getSpadePosition()));			
			}	
		}
}
