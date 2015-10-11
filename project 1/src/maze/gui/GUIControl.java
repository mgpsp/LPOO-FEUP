package maze.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import maze.logic.GameLogic;
import maze.logic.GameState;

public class GUIControl  implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, ActionListener,  Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7404082378905485952L;
	private boolean updated;
	private GameLogic.Movement lastMove = GameLogic.Movement.NONE;
	private boolean throwPressed = false;
	private boolean moving= false;
	private boolean followPlayer = true;
	private static final int THROW_BUTTON = 0;
	private static final int UP_BUTTON = 1;
	private static final int DOWN_BUTTON = 2;
	private static final int LEFT_BUTTON = 3;
	private static final int RIGHT_BUTTON = 4;
	private static final int GAME_TIMER_DELAY = 500;
	private static int[] keys = new int[] { KeyEvent.VK_SPACE,KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT};
	private static int[] savedKeys = keys.clone();
	private GameState g;
	private MazePlay j;
	int rep;
	Point mousePos = new Point();
	Timer timer = new Timer(GAME_TIMER_DELAY, this);
	
	public GUIControl(GameState g, MazePlay j){
		this.g = g;
		this.j = j;
		//timer.start();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		updated = false;
		if(e.getKeyCode() == keys[THROW_BUTTON]){
			throwPressed = true;
			updated = true;
			return;
		}
		if(!throwPressed){
			if(e.getKeyCode() == keys[UP_BUTTON]){
				lastMove =  GameLogic.Movement.UP;
				updated = true;
			}else if(e.getKeyCode() == keys[DOWN_BUTTON]){
				lastMove =  GameLogic.Movement.DOWN;
				updated = true;
			}
			else if(e.getKeyCode() == keys[LEFT_BUTTON]){
				lastMove =  GameLogic.Movement.LEFT;
				updated = true;
			}
			else if(e.getKeyCode() == keys[RIGHT_BUTTON]){
				lastMove =  GameLogic.Movement.RIGHT;
				updated = true;
			}
		}
		else{
			if(e.getKeyCode() == keys[UP_BUTTON]){
				lastMove =  GameLogic.Movement.THROW_UP;
				updated = true;
			}else if(e.getKeyCode() == keys[DOWN_BUTTON]){
				lastMove =  GameLogic.Movement.THROW_DOWN;
				updated = true;
			}
			else if(e.getKeyCode() == keys[LEFT_BUTTON]){
				lastMove =  GameLogic.Movement.THROW_LEFT;
				updated = true;
			}
			else if(e.getKeyCode() == keys[RIGHT_BUTTON]){
				lastMove =  GameLogic.Movement.THROW_RIGHT;
				updated = true;
			}
		}
		if(!updated)
		lastMove =  GameLogic.Movement.NONE;
		updated = true;
		if(GameLogic.winCondition(g)){
			JOptionPane.showMessageDialog(j, "You won!", "Yay!!", JOptionPane.INFORMATION_MESSAGE);
		}
		else GameLogic.gameLoop(g, lastMove);
		
		if(followPlayer)
			MazeDraw.setCamera(g, g.getPlayer().getY(), g.getPlayer().getX(),j.getMazeScreenHRes(), j.getMazeScreenVRes());
		j.repaint();
		timer.restart();
		if(!g.getPlayer().getDraw()){
			rep++;
			if(rep>1){
				JOptionPane.showMessageDialog(j, "You died!", "No!!", JOptionPane.INFORMATION_MESSAGE);
				rep = 0;
			}
		}else rep = 0;
	}
	public void setTimerActivation(boolean activate){
		if(activate)
			timer.start();
		else timer.stop();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		GameLogic.gameLoop(g, GameLogic.Movement.NONE);
		j.repaint();
	}
	public void doGameloop(GameLogic.Movement move){
		GameLogic.gameLoop(g, lastMove);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == keys[THROW_BUTTON]){
			throwPressed = false;
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void mouseDragged(MouseEvent e) {
		if(moving){
			int relx = e.getX() - mousePos.x;
			int rely =  e.getY()- (j.getHeight()-j.getMazeScreenVRes()) - mousePos.y;
			mousePos.x = e.getX();
			mousePos.y  = e.getY()- (j.getHeight()-j.getMazeScreenVRes());
			MazeDraw.getCamera().moveRelScreen(-relx,-rely, j.getMazeScreenHRes(), j.getMazeScreenVRes());
			j.repaint();
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		mousePos.x = e.getX();
		mousePos.y  = e.getY() - (j.getHeight()-j.getMazeScreenVRes());
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON2 && e.getClickCount() >= 2){
			followPlayer = !followPlayer;
			if(followPlayer){
				MazeDraw.resetCamera(g, g.getPlayer().getY(), g.getPlayer().getX(),j.getMazeScreenHRes(), j.getMazeScreenVRes());
				j.repaint();
			}
		//	MazeDraw.resetCamera();
			//j.repaint();
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		//mousePos.x = e.getX();
		//mousePos.y  = e.getY();
	}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON2){
			moving = true;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON2){
			moving = false;
		}
	}
	public static int getThrowButton() {
		return keys[THROW_BUTTON];
	}
	
	public static boolean setButton(int buttonIndex, int newButton){
		for(int i = 0; i < keys.length; i++){
			if(keys[i] == newButton){
				keys[i] = keys[buttonIndex];
				keys[buttonIndex] = newButton;
				return false;
			}
		}
		keys[buttonIndex] = newButton;
		return true;
	}
	
	public static boolean setThrowButton(int newButton) {
		return setButton(THROW_BUTTON, newButton);
	}

	public static int getUpButton() {
		return keys[UP_BUTTON];
	}

	public static boolean setUpButton(int newButton) {
		return setButton(UP_BUTTON, newButton);
	}

	public static int getDownButton() {
		return keys[DOWN_BUTTON];
	}

	public static boolean setDownButton(int newButton) {
		return setButton(DOWN_BUTTON, newButton);
	}

	public static int getLeftButton() {
		return keys[LEFT_BUTTON];
	}

	public static boolean setLeftButton(int newButton) {
		return setButton(LEFT_BUTTON, newButton);
	}

	public static int getRightButton() {
		return keys[RIGHT_BUTTON];
	}

	public static boolean setRightButton(int newButton) {
		return setButton(RIGHT_BUTTON, newButton);
	}
	public static void saveKeys(){
		savedKeys = keys.clone();
	}
	public static void restoreKeys(){
		keys = savedKeys.clone();
	}
	public static void storeKeys(ObjectOutputStream os) throws IOException{
		os.writeObject(keys);
		os.writeObject(savedKeys);
	}
	public static void retrieveKeys(ObjectInputStream is) throws ClassNotFoundException, IOException{
		keys = (int[]) is.readObject();
		savedKeys = (int[]) is.readObject();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if(arg0.getWheelRotation() > 0){
			MazeDraw.getCamera().uncenteredMulScale(1.1, 1.1, mousePos.x, mousePos.y, j.getMazeScreenHRes(), j.getMazeScreenVRes());
			
		}else {
			MazeDraw.getCamera().uncenteredMulScale(.9,.9, mousePos.x, mousePos.y, j.getMazeScreenHRes(), j.getMazeScreenVRes());
		}
		j.repaint();
	}

	public void startTimer(){
		timer.start();
	}
	public  void stopTimer(){
		timer.stop();
	}

}
