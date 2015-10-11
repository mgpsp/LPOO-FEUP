package maze.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import maze.cli.Cli;
import maze.logic.Dart;
import maze.logic.DartQuiver;
import maze.logic.Dragon;
import maze.logic.Entity;
import maze.logic.Fireball;
import maze.logic.GameState;
import maze.logic.Player;
import maze.logic.Shield;
import maze.logic.Sword;
import maze.logic.Tile;

public class MazeDraw {
	
	private static final int IMAGE_BASE = 24;
	private static final int IMAGE_HEIGHT_OFF = 15;
	
	private MazeDraw(){};
	
	private static Date date = new Date();
	private static boolean imagesLoaded = false;
	
	private static BufferedImage buffer;
	private static Graphics2D bufferg;
	private static Camera c;
	
	//http://opengameart.org/content/a-blocky-dungeon
	//
	private static BufferedImage dragonImage;
	private static BufferedImage dragonAsleepImage;
	private static BufferedImage dragonDeadImage;
	
	private static BufferedImage dartImage;
	private static BufferedImage dartpickedImage;
	private static BufferedImage dartupImage;
	private static BufferedImage dartdownImage;
	private static BufferedImage dartleftImage;
	private static BufferedImage dartrightImage;
	
	private static BufferedImage fireballImage;
	
	private static BufferedImage shieldImage;
	private static BufferedImage swordImage;
	private static BufferedImage swordnotvisibleImage;
	private static BufferedImage unarmedHeroImage;
	private static BufferedImage swordHeroImage;
	private static BufferedImage shieldHeroImage;
	private static BufferedImage swordshieldHeroImage;
	private static BufferedImage deadHeroImage;
	
	private static BufferedImage topright;
	private static BufferedImage topleft;
	private static BufferedImage top;
	private static BufferedImage bottomleft;
	private static BufferedImage bottomright;
	private static BufferedImage bottomleftextra;
	private static BufferedImage bottomrightextra;
	private static BufferedImage toprightrightnowall;
	private static BufferedImage topleftleftnowall;
	private static BufferedImage bottom;
	private static BufferedImage right;
	private static BufferedImage left;
	
	private static BufferedImage blackdefault;
	private static BufferedImage blackright;
	private static BufferedImage blackleft;
	private static BufferedImage blacktop;
	private static BufferedImage blacktopleft;
	private static BufferedImage blacktopright;
	private static BufferedImage blackbottom;
	private static BufferedImage blackbottomright;
	private static BufferedImage blackbottomleft;
	
	private static BufferedImage shadowbottom;
	private static BufferedImage shadowtop;
	private static BufferedImage shadowside;
	
	private static BufferedImage rocks1;
	private static BufferedImage rocks2;
	private static BufferedImage rocks3;
	private static BufferedImage rocks4;
	
	private static BufferedImage walldecoration1;
	private static BufferedImage walldecoration2;
	private static BufferedImage walldecoration3;
	private static BufferedImage walldecoration4;
	
	private static BufferedImage exitOpen;
	private static BufferedImage exitClosed;
	
	private static BufferedImage floor;
	
	public static boolean loadImages(){
		if(imagesLoaded)
			return true;
		try{
			
				dragonImage = ImageIO.read(new File("./res/dragon.png"));
				dragonAsleepImage= ImageIO.read(new File("./res/dragonsleep.png"));
				dragonDeadImage= ImageIO.read(new File("./res/dragondead.png"));
				shieldImage = ImageIO.read(new File("./res/shield.png"));
				swordImage= ImageIO.read(new File("./res/swordvisible.png"));
				swordnotvisibleImage= ImageIO.read(new File("./res/swordnotvisible.png"));
				unarmedHeroImage= ImageIO.read(new File("./res/h_unarmed.png"));
				swordHeroImage= ImageIO.read(new File("./res/h_sword.png"));
				shieldHeroImage= ImageIO.read(new File("./res/h_shield.png"));
				swordshieldHeroImage= ImageIO.read(new File("./res/h_sword_shield.png"));
				deadHeroImage= ImageIO.read(new File("./res/h_dead.png"));
				dartImage = ImageIO.read(new File("./res/dart.png"));
				dartpickedImage = ImageIO.read(new File("./res/dartpicked.png"));
				dartupImage= ImageIO.read(new File("./res/dartup.png"));
				dartleftImage= ImageIO.read(new File("./res/dartleft.png"));
				dartrightImage = ImageIO.read(new File("./res/dartright.png"));
				dartdownImage = ImageIO.read(new File("./res/dartdown.png"));
				
				fireballImage = ImageIO.read(new File("./res/fireball.png"));
				
				topright= ImageIO.read(new File("./res/topright.png"));
				topleft= ImageIO.read(new File("./res/topleft.png"));
				top = ImageIO.read(new File("./res/top.png"));
				bottomleft= ImageIO.read(new File("./res/bottomleft.png"));
				bottomright= ImageIO.read(new File("./res/bottomright.png"));
				bottomleftextra= ImageIO.read(new File("./res/bottomleftextra.png"));
				bottomrightextra= ImageIO.read(new File("./res/bottomrightextra.png"));
				toprightrightnowall= ImageIO.read(new File("./res/toprightright-nowall.png"));
				topleftleftnowall= ImageIO.read(new File("./res/topleftleft-nowall.png"));
				right= ImageIO.read(new File("./res/right.png"));
				left= ImageIO.read(new File("./res/left.png"));
				bottom= ImageIO.read(new File("./res/bottom.png"));
				
				blackdefault= ImageIO.read(new File("./res/defaultblack.png"));
				blackright= ImageIO.read(new File("./res/blackright.png"));
				blacktop= ImageIO.read(new File("./res/blacktop.png"));
				blacktopleft= ImageIO.read(new File("./res/blacktopleft.png"));
				blacktopright= ImageIO.read(new File("./res/blacktopright.png"));
				blackleft= ImageIO.read(new File("./res/blackleft.png"));
				blackbottom= ImageIO.read(new File("./res/blackbottom.png"));
				blackbottomright= ImageIO.read(new File("./res/blackbottomright.png"));
				blackbottomleft= ImageIO.read(new File("./res/blackbottomleft.png"));
				
				shadowbottom= ImageIO.read(new File("./res/shadowbottom.png"));
				shadowtop= ImageIO.read(new File("./res/shadowtop.png"));
				shadowside= ImageIO.read(new File("./res/shadowside.png"));
				
				
				exitClosed= ImageIO.read(new File("./res/exitclosed.png"));
				exitOpen= ImageIO.read(new File("./res/exitopen.png"));
				
				rocks1= ImageIO.read(new File("./res/rocks1.png"));
				rocks2= ImageIO.read(new File("./res/rocks2.png"));
				rocks3= ImageIO.read(new File("./res/rocks3.png"));
				rocks4= ImageIO.read(new File("./res/rocks4.png"));
				
				 walldecoration1= ImageIO.read(new File("./res/walldecoration1.png"));
				 walldecoration2= ImageIO.read(new File("./res/walldecoration2.png"));
				 walldecoration3= ImageIO.read(new File("./res/walldecoration3.png"));
				 walldecoration4= ImageIO.read(new File("./res/walldecoration4.png"));
				
				floor = ImageIO.read(new File("./res/floor.png"));

		} 
		catch(IOException e){
			return false;
		}	
		imagesLoaded = true;
		return true;
	}
	public static void drawCentered(Graphics g, GameState gs, int availableX, int availableY, int finalX, int finalY, int windowWidth, int windowHeight){
		int sizex = (finalX-availableX)/gs.getLabyrithm().getWidth();
		int sizey = (finalY-availableY)/gs.getLabyrithm().getHeight();
		int size = Math.min(sizex, sizey);
		int initialX = availableX/2 + (windowWidth - size*gs.getLabyrithm().getWidth())/2;
		int initialY = availableY/2 +(windowHeight - size*gs.getLabyrithm().getHeight())/2;
		draw(g, gs, initialX, initialY, size);
	}
	public static void drawCenteredLeft(Graphics g, GameState gs, int availableX, int availableY, int finalX, int finalY, int windowWidth, int windowHeight){
		int sizex = (finalX-availableX)/gs.getLabyrithm().getWidth();
		int sizey = (finalY-availableY)/gs.getLabyrithm().getHeight();
		int size = Math.min(sizex, sizey);
		int initialX = availableX;
		int initialY = availableY/2 +(windowHeight - size*gs.getLabyrithm().getHeight())/2;
		draw(g, gs, initialX, initialY, size);
	}
	public static boolean isFreeTile(GameState g, int tilex, int tiley){
		if(tiley < 0 || tiley >= g.getLabyrithm().getWidth())
			return false;
		if(tilex < 0 || tilex >= g.getLabyrithm().getHeight())
			return false;
		if(g.getLabyrithm().getTileType(tilex, tiley) == Tile.TileType.WALL)
			return false;
		return true;
	}
	public static Point getRenderPosition(int x, int y,int initialX, int initialY, int size){
		return new Point(y*size + initialX, x*size + initialY);
	}
	
	public static void displayTileComponent(Graphics g, BufferedImage component, Point p, int size){
		g.drawImage(component, p.x, (int)(p.y-size*.625),  (int)(p.x + size) ,(int)(p.y + size), 0, 0, component.getWidth(), component.getHeight(), null);	
	}
	
	public static void drawTileWalls(Graphics g, GameState gs, int tilex, int tiley, Point p, int size){
		if(gs.getLabyrithm().getTileType(tilex, tiley) == Tile.TileType.WALL){
			boolean topFree = isFreeTile(gs, tilex-1, tiley);
			boolean bottomFree = isFreeTile(gs, tilex+1, tiley);
			boolean rightFree = isFreeTile(gs, tilex, tiley+1);
			boolean leftFree = isFreeTile(gs, tilex, tiley-1);
			//displayTileComponent(g, floor, p, size );
			if(!topFree){
				displayTileComponent(g, blacktop, p, size );
			}
			if(!leftFree){
				displayTileComponent(g, blackleft, p, size );
			}
			if(!rightFree){
				displayTileComponent(g, blackright, p, size );
			}
			if(!rightFree && !topFree){
				displayTileComponent(g, blacktopright, p, size );
			}
			if(!leftFree && !topFree){
				displayTileComponent(g, blacktopleft, p, size );
			}
			if(!bottomFree){
				displayTileComponent(g, blackbottom, p, size );
			}
			if(!bottomFree && ! rightFree){
				displayTileComponent(g, blackbottomright, p, size );
			}
			if(!bottomFree && ! leftFree){
				displayTileComponent(g, blackbottomleft, p, size );
			}
			
			displayTileComponent(g, blackdefault, p, size );
		
			if(isFreeTile(gs, tilex+1, tiley)){
				displayTileComponent(g, bottom, p, size );
				int hash = (tilex * tiley) + (tilex * tilex) + (tiley * tiley)+1;
				hash *= date.hashCode() * date.hashCode();
				hash = new Random(hash).nextInt();
				if(hash % 7 == 1)
					displayTileComponent(g, walldecoration1, p, size);
				if(hash % 7 == 2)
					displayTileComponent(g, walldecoration2, p, size);
				if(hash % 7 == 3)
					displayTileComponent(g, walldecoration3, p, size);
				if(hash % 7 == 4)
					displayTileComponent(g, walldecoration4, p, size);
			}
			if(isFreeTile(gs, tilex, tiley-1)){
				displayTileComponent(g, left, p, size );
			}
			if(isFreeTile(gs, tilex-1, tiley)){
				displayTileComponent(g, top, p, size );
			}
			if(isFreeTile(gs, tilex, tiley+1)){
				displayTileComponent(g, right, p, size );
				p.x += size-.125*size;
				displayTileComponent(g, shadowside, p, size);
			}
		}
	}
	public static void drawTileCorners(Graphics g, GameState gs, int tilex, int tiley, Point p, int size){
		if(gs.getLabyrithm().getTileType(tilex, tiley) == Tile.TileType.WALL){
			boolean displayedBottom = false;
			boolean displayedLeft = false;
			boolean displayedRight = false;
			boolean displayedTop= false;
			if(isFreeTile(gs, tilex+1, tiley)){
				displayedBottom = true;
			}
			if(isFreeTile(gs, tilex, tiley-1)){
				displayedLeft = true;
			}
			if(isFreeTile(gs, tilex, tiley+1)){
				displayedRight = true;
			}
			if(isFreeTile(gs, tilex-1, tiley)){
				displayedTop= true;
			}
			if( (isFreeTile(gs, tilex-1, tiley-1)&&!(displayedTop|| displayedLeft)|| (displayedTop&&displayedLeft))){
				displayTileComponent(g, topleft, p, size );
				if(displayedLeft){
					displayTileComponent(g, topleftleftnowall, p, size );
				}
			}
			if( (isFreeTile(gs, tilex-1, tiley+1)&&!(displayedTop || displayedRight)|| (displayedTop && displayedRight))){
				displayTileComponent(g, topright, p, size );
				if(displayedRight){
					displayTileComponent(g, toprightrightnowall, p, size );
				}
			}
			if( (isFreeTile(gs, tilex+1, tiley+1) &&!(displayedBottom || displayedRight) || (displayedBottom && displayedRight))){
				displayTileComponent(g, bottomright, p, size );
				if(displayedBottom)
					displayTileComponent(g, bottomrightextra, p, size );
			}
			if( (isFreeTile(gs, tilex+1, tiley-1)&&!(displayedBottom || displayedLeft) || (displayedBottom && displayedLeft) )){
				displayTileComponent(g, bottomleft, p, size );
				if(displayedBottom)
					displayTileComponent(g, bottomleftextra, p, size );
			}
		}
	}
	public static void drawTileFloor(Graphics g, GameState gs, int tilex, int tiley, Point p, int size){
		displayTileComponent(g, floor, p, size);
		if(gs.getLabyrithm().getTileType(tilex, tiley) == Tile.TileType.FLOOR){
			int hash = (tilex * tiley) + (tilex * tilex) + (tiley * tiley);
			hash *= date.hashCode();
			hash = new Random(hash).nextInt();
			if(hash % 15 == 1)
				displayTileComponent(g, rocks1, p, size);
			if(hash % 15 == 2)
				displayTileComponent(g, rocks2, p, size);
			if(hash % 15 == 3)
				displayTileComponent(g, rocks3, p, size);
			if(hash % 15 == 4)
				displayTileComponent(g, rocks4, p, size);
		}
		else if(gs.getLabyrithm().getTileType(tilex, tiley) == Tile.TileType.EXIT){
			if(gs.getPlayer() != null && gs.getPlayer().isArmed() && gs.getLiveDragons() == 0)
				displayTileComponent(g, exitOpen, p, size);
			else displayTileComponent(g, exitClosed, p, size);
		}
		if(!isFreeTile(gs, tilex, tiley-1)){
			boolean drawShadowtop = isFreeTile(gs, tilex-1, tiley-1);
			boolean drawShadowbottom = (isFreeTile(gs, tilex+1, tiley) &&!(isFreeTile(gs, tilex+1, tiley-1) || isFreeTile(gs, tilex, tiley)) || (isFreeTile(gs, tilex+1, tiley-1) && isFreeTile(gs, tilex, tiley)));//isFreeTile(gs, tilex+1, tiley-1);
			if(drawShadowtop)
				displayTileComponent(g, shadowtop, p, size);
			if(drawShadowbottom)
				displayTileComponent(g, shadowbottom, p, size);
		}
		
	}
	
	public static void drawEntityScreen(Graphics g,GameState gs,  Entity t, int initialX, int initialY, int size){
		drawEntity(g,gs, t, getRenderPosition(t.getX(), t.getY(),initialX,initialY,size), size);
	}
	public static void drawTileScreenWalls(Graphics g, GameState gs, int tilex, int tiley, int initialX, int initialY, int size){
		drawTileWalls(g, gs, tilex, tiley, getRenderPosition(tilex, tiley ,initialX,initialY,size),size);
	}
	public static void drawTileScreenCorners(Graphics g, GameState gs, int tilex, int tiley, int initialX, int initialY, int size){
		drawTileCorners(g, gs, tilex, tiley, getRenderPosition(tilex, tiley ,initialX,initialY,size),size);
	}
	public static void drawTileScreenFloor(Graphics g, GameState gs, int tilex, int tiley, int initialX, int initialY, int size){
		drawTileFloor(g, gs, tilex, tiley, getRenderPosition(tilex, tiley ,initialX,initialY,size),size);
	}
	
	
	public static void drawEntity(Graphics g,GameState gs,  Entity t,  Point p, int size){
		if(t == null)
			return;
		if(!imagesLoaded)
			loadImages();
		if(t instanceof Player){
			if(!t.getDraw()){
				displayTileComponent(g, deadHeroImage, p, size );
			}
			else if(((Player) t).isArmed() && ((Player) t).isShielded())
				displayTileComponent(g, swordshieldHeroImage, p, size );
			else if(((Player) t).isArmed())
				displayTileComponent(g, swordHeroImage, p, size );
			else if(((Player) t).isShielded())
				displayTileComponent(g, shieldHeroImage, p, size );
			else displayTileComponent(g, unarmedHeroImage, p, size );
			
		}
		else if(t instanceof Dragon){
			if(t.getDraw()){
				if(((Dragon) t).getAsleep()){
					displayTileComponent(g, dragonAsleepImage, p, size );
					Font f = g.getFont();
					f = new Font(Font.SANS_SERIF, Font.PLAIN, size/6);
					g.setFont(f);
					g.setColor(Color.GREEN);
				
					//g.drawString(Integer.toString(((Dragon) t).getSleepcycles()), p.x, p.y);
				}
				else displayTileComponent(g, dragonImage, p, size );
			}else displayTileComponent(g, dragonDeadImage, p, size );
		}
		else if(t instanceof Fireball){
			if(isFreeTile(gs, t.getX(), t.getY()))
			displayTileComponent(g, fireballImage, p, size );
		}
		else if(t instanceof Dart){
			if(isFreeTile(gs, t.getX(), t.getY())){
				if(!((Dart) t).getHorizontal_movement()){
					if(((Dart) t).getPositive_movement())
						displayTileComponent(g, dartrightImage, p, size );
					else displayTileComponent(g, dartleftImage, p, size );
						
				}else {
					if(((Dart) t).getPositive_movement())
						displayTileComponent(g, dartdownImage, p, size );
					else displayTileComponent(g, dartupImage, p, size );
				}
			}
		}
		else if(t instanceof Shield){
			if(t.getDraw())
			displayTileComponent(g, shieldImage, p, size );
		}
		else if(t instanceof Sword){
			if(t.getDraw())
				displayTileComponent(g, swordImage, p, size );
			else displayTileComponent(g, swordnotvisibleImage, p, size );
		}
		else if(t instanceof DartQuiver){
			if(t.getDraw())
				displayTileComponent(g, dartImage, p, size );
			else displayTileComponent(g, dartpickedImage, p, size );
		}
	}
	
	public static void draw(Graphics g, GameState gs, int initialX, int initialY, int size){
		if(!imagesLoaded)
			if(!loadImages())
				return;
		PriorityQueue<Entity> toPrint = Cli.getPrintQueue(gs);
		for(int i = 0; i < gs.getLabyrithm().getWidth(); i++){
			for(int j = 0; j < gs.getLabyrithm().getHeight(); j++)
				drawTileScreenFloor(g,gs, i, j, initialX, initialY, size);

			for(int j = 0; j < gs.getLabyrithm().getHeight(); j++)
				drawTileScreenWalls(g,gs, i, j,initialX, initialY, size);

			for(int j = 0; j < gs.getLabyrithm().getHeight(); j++)
				drawTileScreenCorners(g,gs, i, j,initialX, initialY, size);

			while(!toPrint.isEmpty()){
				if(toPrint.peek().getX() == i){
					drawEntityScreen(g, gs, toPrint.peek(), initialX, initialY,  size);
					toPrint.remove();
				} else break;
			}
		}
	}
	
	public static void drawCamera2(Graphics g, GameState gs, int initialX, int initialY, int resX, int resY){
		if(!imagesLoaded)
			loadImages();
		if(c == null)
			c = new Camera(0, 0, getBufferWidth(gs), getBufferWidth(gs), IMAGE_BASE*4, IMAGE_BASE*4 + IMAGE_HEIGHT_OFF, getBufferWidth(gs) * 2, getBufferWidth(gs)* 2);
		int mini, maxi, minj, maxj;
		minj = (int) (c.getX()/IMAGE_BASE)-1;
		maxj = (int) (c.getFinalX() / IMAGE_BASE )+1;
		mini = (int) (c.getY()/IMAGE_BASE)-1;
		maxi = (int) (c.getFinalY() / IMAGE_BASE )+2;
		buffer = new BufferedImage((maxj - minj) * IMAGE_BASE , (maxi - mini) * IMAGE_BASE + IMAGE_HEIGHT_OFF, BufferedImage.TYPE_INT_ARGB);
			bufferg = buffer.createGraphics();

		int size = IMAGE_BASE;
		int bufferInitialX = -(minj) *IMAGE_BASE;
		int bufferInitialY = -(mini) *IMAGE_BASE ;
		PriorityQueue<Entity> toPrint = Cli.getPrintQueue(gs);
		for(int i = 0; i < gs.getLabyrithm().getWidth(); i++){
			for(int j = 0; j < gs.getLabyrithm().getHeight(); j++)
				if( j > minj && j < maxj && i > mini && i < maxi)
					drawTileScreenFloor(bufferg,gs, i, j, bufferInitialX, bufferInitialY, size);

			for(int j = 0; j < gs.getLabyrithm().getHeight(); j++)
				if( j > minj && j < maxj && i > mini && i < maxi)
					drawTileScreenWalls(bufferg,gs, i, j,bufferInitialX, bufferInitialY, size);

			for(int j = 0; j < gs.getLabyrithm().getHeight(); j++)
				if( j > minj && j < maxj && i > mini && i < maxi)
					drawTileScreenCorners(bufferg,gs, i, j, bufferInitialX, bufferInitialY, size);

			while(!toPrint.isEmpty()){
				if(toPrint.peek().getX() == i){
					drawEntityScreen(bufferg, gs, toPrint.peek(), bufferInitialX, bufferInitialY,  size);
					toPrint.remove();
				} else break;
			}
		}
		double posix = (int)(c.getX() - minj * IMAGE_BASE);
		double posiy=(int) (c.getY() - mini *IMAGE_BASE);
		g.drawImage(buffer, initialX, initialY, resX, resY, (int)posix ,(int)posiy, (int)(posix +c.getWidth()), (int)(posiy + c.getHeight()), null);
	}

	public static int getBufferWidth(GameState gs){
		return (gs.getLabyrithm().getHeight() * IMAGE_BASE);
	}
	public static int getBufferHeight(GameState gs){
		return  (gs.getLabyrithm().getWidth() * IMAGE_BASE + IMAGE_HEIGHT_OFF);
	}
	public static Camera getCamera(){
		return c;
	}
	public static void resetCamera(){
		if(buffer == null)
			return;
		c = new Camera(0, 0, buffer.getWidth(), buffer.getHeight(), IMAGE_BASE*4, IMAGE_BASE*4 + IMAGE_HEIGHT_OFF, buffer.getWidth() * 2, buffer.getHeight() * 2);
	}
	public static void resetCamera(GameState gs){
		c = new Camera(0, 0, getBufferWidth(gs), getBufferWidth(gs), IMAGE_BASE*4, IMAGE_BASE*4 + IMAGE_HEIGHT_OFF, getBufferWidth(gs) * 4, getBufferWidth(gs)* 4);
	}
	public static void resetCamera(GameState gs, int x, int y, int h_res, int v_res){
		c = new Camera(IMAGE_BASE*x-5*IMAGE_BASE, IMAGE_BASE*y- 5*IMAGE_BASE-IMAGE_HEIGHT_OFF, IMAGE_BASE*x + 5*IMAGE_BASE,IMAGE_BASE*y + 5*IMAGE_BASE, IMAGE_BASE*4, IMAGE_BASE*4 + IMAGE_HEIGHT_OFF, getBufferWidth(gs) * 4, getBufferWidth(gs)* 4);
		//double ratio = ((double) getBufferWidth(gs)) / (getBufferHeight(gs));
		double ratio =  ((double)h_res) / (v_res);
		c.mulScale(ratio, 1);
		c.movePartialAbsCentered(IMAGE_BASE*(x+1)+IMAGE_BASE/2, IMAGE_BASE*(y+1)+IMAGE_BASE/2 , h_res, v_res, 1);
	}
	public static Dimension getMinimumSize() {
		return new Dimension(IMAGE_BASE*12, IMAGE_BASE*4 + IMAGE_HEIGHT_OFF + 100);
	}
	public static void setCamera(GameState g, int x, int y, int mazeScreenHRes,	int mazeScreenVRes) {
		c.moveAbsCentered((x+.5) * IMAGE_BASE, (y+ .5)* IMAGE_BASE);
			
	}
}
