package maze.gui;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;



import maze.logic.GameState;

public class MainMenu extends MazeScreen{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6090894998739332157L;

	public MainMenu(GameState g) throws IOException {
		super(g);
		JButton continueButton = new JButton("Continue");
		JButton playButton = new JButton("New Game");
		JButton loadButton = new JButton("Load Game");
		JButton editButton = new JButton("Level Editor");
		JButton settingsButton = new JButton("Settings");
		JButton quitButton = new JButton("Quit");
		this.setLayout(new GridLayout(6,1, 10, 5));
		this.add(continueButton);
		this.add(playButton);
		this.add(loadButton);
		this.add(editButton);
		this.add(settingsButton);
		this.add(quitButton);
		
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int result = JOptionPane.showConfirmDialog(quitButton, "Are you sure you want to quit?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION)
					notifyObservers(MazeWindow.Screen.CLOSE);
				MainMenu.this.requestFocus();
				MainMenu.this.repaint();
			}
		});
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!MazeRegen.requestRegen(playButton, getGameState(), () -> notifyObservers(MazeWindow.Screen.GAME)))
					JOptionPane.showMessageDialog(playButton, "Proccessing!");
				
			}
		});
		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyObservers(MazeWindow.Screen.GAME);
			}
		});
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(".").getCanonicalFile());
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(loadButton);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					SaveLoad.loadGame(fc.getSelectedFile(),  getGameState());
					notifyObservers(MazeWindow.Screen.GAME);
				}
				
			}
		});
		settingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyObservers(MazeWindow.Screen.SETTINGS);
			}
		});
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyObservers(MazeWindow.Screen.EDITOR);
			}
		});
		
		this.setPreferredSize(new Dimension(300,500));
		setFocusable(false);
	}
	
}
