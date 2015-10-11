package maze.gui;

import java.util.Observable;
import java.util.Observer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import maze.logic.GameState;


public class MazeWindow extends JFrame implements Observer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7088798643641972272L;
	
	private static MazeWindow instance = null;
	private GameState gs;
	public enum Screen{GAME, MAIN, SETTINGS, EDITOR, CLOSE};
	private MazePlay mazePlay;
	private MainMenu mainMenu;
	private JDialog settingswindow;
	private SettingsMenu settings;
	private Editor editor;
	
	public static MazeWindow getInstance() throws IOException{
		if(instance == null)
			instance = new MazeWindow();
		return instance;
	}
	
	private MazeWindow() throws IOException{
		super("Maze");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}
		SaveLoad.loadGame(new File("running_session_maze"), null);
		SaveLoad.loadKeyBindings(new File("running_session_keys"));
		if(gs == null)
			gs = new GameState(15,3);
		
		this.setPreferredSize(new Dimension(250,500));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowListener(){
			@Override
			public void windowActivated(WindowEvent arg0) {}
			@Override
			public void windowClosed(WindowEvent arg0) {}
			@Override
			public void windowClosing(WindowEvent arg0) {
				SaveLoad.saveGame(new File("running_session_maze"), gs);
				SaveLoad.saveKeyBindings(new File("running_session_keys"));
			}
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			@Override
			public void windowIconified(WindowEvent arg0) {}
			@Override
			public void windowOpened(WindowEvent arg0) {}
		});

		getContentPane().setLayout(new CardLayout());
		mazePlay = new MazePlay(gs);
		mainMenu = new MainMenu(gs);
		settings = new SettingsMenu(gs);
		editor = new Editor(gs);
		this.addComponentListener(new ComponentListener(){
			@Override
			public void componentHidden(ComponentEvent arg0) {}
			@Override
			public void componentMoved(ComponentEvent e) {
				mazePlay.windowMove(MazeWindow.this.getLocationOnScreen());
			}
			@Override
			public void componentResized(ComponentEvent e) {}

			@Override
			public void componentShown(ComponentEvent e) {}
		});
		mazePlay.addObserver((Observer)this);
		mainMenu.addObserver((Observer)this);
		settings.addObserver((Observer)this);
		editor.addObserver((Observer)this);

		settingswindow = new JDialog();
		settingswindow.setTitle("Settings");
		settingswindow.getContentPane().add(settings);
		settingswindow.setSize(settings.getPreferredSize());
		settingswindow.setResizable(false);

		this.setPreferredSize(mainMenu.getPreferredSize());
		this.getContentPane().add(mainMenu);
		this.getContentPane().add(mazePlay);
		this.getContentPane().add(editor);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
		this.setMinimumSize(MazeDraw.getMinimumSize());
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension initial = new Dimension((int)(screen.getWidth() /2 - getPreferredSize().getWidth()/2),(int)( screen.getHeight() /2 -getPreferredSize().getHeight()/2));
		this.setLocation(initial.width, initial.height);
		settingswindow.setLocation(initial.width, initial.height);
		mazePlay.requestFocus();	
	}
	GameState getGameState (){
		return gs;
	}
	public void update(Observable o, Object c){
		if(!(c instanceof Screen))
			return;
		for(Component comp : this.getContentPane().getComponents()){
			comp.setVisible(false);
		}
		settingswindow.setVisible(false);
		mazePlay.stopTimer();
		switch((Screen)c){
		case GAME:
			this.setSize(mazePlay.getPreferredSize());
			mazePlay.setVisible(true);
			mazePlay.requestFocus();
			this.setResizable(true);
			mazePlay.startTimer();
			break;
		case MAIN:
			mainMenu.setVisible(true);
			this.setResizable(false);
			this.setSize(mainMenu.getPreferredSize());
			break;
		case SETTINGS:
			settingswindow.setVisible(true);
			break;
		case EDITOR:
			this.setSize(editor.getPreferredSize());
			editor.setVisible(true);
			editor.requestFocus();
			this.setResizable(true);
			break;
		case CLOSE:
			this.processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension initial = new Dimension((int)(screen.getWidth() /2 -getSize().getWidth()/2),(int)( screen.getHeight() /2 -getSize().getHeight()/2));
		this.setLocation(initial.width, initial.height);
	}
	
}
