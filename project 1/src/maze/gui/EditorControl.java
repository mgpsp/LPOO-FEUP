package maze.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComboBox;



import maze.logic.DartQuiver;
import maze.logic.Dragon;
import maze.logic.GameState;
import maze.logic.Item;
import maze.logic.Pair;
import maze.logic.Player;
import maze.logic.Shield;
import maze.logic.Sword;
import maze.logic.Tile;

public class EditorControl implements MouseListener, MouseMotionListener, KeyListener, ActionListener{
	private Editor j;
	private boolean drawing = false;
	private boolean erasing = false;
	private Class<?> editing;
	public EditorControl(Editor j){
		this.j = j;
	}
	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		GameState g = j.getEditing();
		if(g == null)
			return;
		int x, y;
		int size = j.getTileSize();
		x = (e.getX()-j.getInitialX())/size;
		y = (e.getY()-j.getInitialY())/size;
		if(x >= 0 && x < g.getLabyrithm().getHeight() && y >= 0 && y < g.getLabyrithm().getWidth()){
			
			if((x == 0 || x == g.getLabyrithm().getHeight()-1) || (y == 0 || y == g.getLabyrithm().getWidth()-1))
				return;
			if(drawing){
				if(editing == Tile.class)
					if(g.getLabyrithm().getTileType(y, x) == Tile.TileType.FLOOR && g.tileOccupied(y, x) == null){
						drawing = true;
						g.getLabyrithm().getTile(y, x).setType(Tile.TileType.WALL);
					}
			}
			else if(erasing) 
				if(editing == Tile.class)
					if(g.getLabyrithm().getTileType(y, x) == Tile.TileType.WALL){
						erasing = true;
						g.getLabyrithm().getTile(y, x).setType(Tile.TileType.FLOOR);
					}
			j.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		GameState g = j.getEditing();
		if(g == null)
			return;
		int x, y;
		int size = j.getTileSize();
		x = (e.getX()-j.getInitialX())/size;
		y = (e.getY()-j.getInitialY())/size;
		
		if(x >= 0 && x < g.getLabyrithm().getHeight() && y >= 0 && y < g.getLabyrithm().getWidth()){
			if((x == 0 || x == g.getLabyrithm().getHeight()-1) && (y == 0 || y == g.getLabyrithm().getWidth()-1))
				return;
			if(((x == 0 || x == g.getLabyrithm().getHeight()-1) && !(y == 0 || y == g.getLabyrithm().getWidth()-1)) || (!(x == 0 || x == g.getLabyrithm().getHeight()-1) && (y == 0 || y == g.getLabyrithm().getWidth()-1))){
				Pair<Integer, Integer> exit = g.getLabyrithm().getExitPosition();
				g.getLabyrithm().getTile(y, x).setType(Tile.TileType.EXIT);
				if(exit != null && !(exit.first == y && exit.second == x))
					g.getLabyrithm().getTile(exit.first,exit.second).setType(Tile.TileType.WALL);
				j.repaint();
				return;
			}
			if(e.getButton() == MouseEvent.BUTTON1){
				drawing = true;
				if(editing == Tile.class){
					if(g.getLabyrithm().getTileType(y, x) == Tile.TileType.FLOOR && g.tileOccupied(y, x) == null){
						g.getLabyrithm().getTile(y, x).setType(Tile.TileType.WALL);
					}
				}
				else if(editing == Player.class){
					if(g.tileOccupied(y, x) == null && g.getLabyrithm().getTileType(y, x) == Tile.TileType.FLOOR){
						g.setPlayer(new Player(y,x));
					}
				}
				else if(editing == Dragon.class){
					if(g.tileOccupied(y, x) == null && g.getLabyrithm().getTileType(y, x) == Tile.TileType.FLOOR){
						g.addDragon(new Dragon(y,x));
					}
				}
				else if(editing == Shield.class){
					if(g.tileOccupied(y, x) == null && g.getLabyrithm().getTileType(y, x) == Tile.TileType.FLOOR){
						g.addItem(new Shield(y,x));
					}
				}
				else if(editing == Sword.class){
					if(g.tileOccupied(y, x) == null && g.getLabyrithm().getTileType(y, x) == Tile.TileType.FLOOR){
						g.addItem(new Sword(y,x));
					}
				}
				else if(editing == DartQuiver.class){
					if(g.tileOccupied(y, x) == null && g.getLabyrithm().getTileType(y, x) == Tile.TileType.FLOOR){
						g.addItem(new DartQuiver(y,x,1));
					}
				}
			}
			else if(e.getButton() == MouseEvent.BUTTON3){
				erasing = true;
				if(editing == Tile.class){
					if(g.getLabyrithm().getTileType(y, x) == Tile.TileType.WALL){
						g.getLabyrithm().getTile(y, x).setType(Tile.TileType.FLOOR);
					}
				}else if(editing == Dragon.class){
					for(Dragon d : g.getDragons()){
						if(d.getX() == y && d.getY() == x){
							g.removeDragon(d);
							break;
						}
					}
				}
				else if(editing == Shield.class){
					for(Item d : g.getItems()){
						if(d instanceof Shield && d.getX() == y && d.getY() == x){
							g.removeItem(d);
							break;
						}
					}
				}
				else if(editing == Sword.class){
					for(Item d : g.getItems()){
						if(d instanceof Sword && d.getX() == y && d.getY() == x){
							g.removeItem(d);
							break;
						}
					}
				}
				else if(editing == DartQuiver.class){
					for(Item d : g.getItems()){
						if(d instanceof DartQuiver && d.getX() == y && d.getY() == x){
							g.removeItem(d);
							break;
						}
					}
				}
			}
			j.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		drawing = false;
		erasing = false;
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if((arg0.getSource() instanceof JComboBox)){
			@SuppressWarnings("unchecked")
			String selected = (String) ((JComboBox<String>)arg0.getSource()).getSelectedItem();
			if(selected.equals("Tile")){
				editing = Tile.class;
			}else if(selected.equals("Dragon")){
				editing = Dragon.class;
			}else if(selected.equals("Player")){
				editing = Player.class;
			}else if(selected.equals("Shield")){
				editing = Shield.class;
			}else if(selected.equals("Sword")){
				editing = Sword.class;
			}else if(selected.equals("Dart")){
				editing = DartQuiver.class;
			}
		}
	}
	
}

