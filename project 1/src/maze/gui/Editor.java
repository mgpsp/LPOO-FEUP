package maze.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



import maze.logic.*;

public class Editor extends MazeScreen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7053673461787423075L;
	JPanel bottomPanel;
	GameState editing;
	public Editor(GameState gs) throws IOException {
		super(gs);
		EditorControl c = new EditorControl(this);
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		addButtons();
		addEditorPanelComponents(c);
		this.addMouseListener(c);
		this.addMouseMotionListener(c);
		this.addKeyListener(c);
		this.setPreferredSize(new Dimension(800,800));
	}
	private void addEditorPanelComponents(EditorControl c) {
		JComboBox<String> entities = new JComboBox<String>();
		entities.addActionListener(c);
		entities.addItem("Tile");
		entities.addItem("Dragon");
		entities.addItem("Player");
		entities.addItem("Shield");
		entities.addItem("Sword");
		entities.addItem("Dart");
		setLayout(new BorderLayout());	
		bottomPanel.add(new JLabel("Editing: "));
		bottomPanel.add(entities);
		add(bottomPanel, BorderLayout.NORTH);
		
	}
	private void addButtons() throws IOException {
		JButton btnCloseButton = new JButton("Close");
		btnCloseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(btnCloseButton, "Are you sure you want to quit?", "Confirm", JOptionPane.OK_CANCEL_OPTION);//JOptionPane.showConfirmDialog(btnCloseButton, "Are you sure you want to quit?");
				if(result == JOptionPane.OK_OPTION)
					notifyObservers(MazeWindow.Screen.CLOSE);
				Editor.this.requestFocus();
				Editor.this.repaint();
			}
		});
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyObservers(MazeWindow.Screen.MAIN);
				Editor.this.requestFocus();
				Editor.this.repaint();
			}
		});
		JButton newButton = new JButton("New Level");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(editing != null){
					int result = JOptionPane.showConfirmDialog(newButton, "Are you sure you want to overwrite the current level?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
					if(result != JOptionPane.OK_OPTION){
						return;
					}
				}
				boolean retry = true;
				int size = 0;
				while(retry){
					String s = JOptionPane.showInputDialog(newButton, "Please enter size of maze");
					if(s == null){
						break;
					}
					try{
						size = Integer.parseInt(s);
					}catch(Exception except){}
					if(size > 0 && size < 687){
						try{
							editing = new GameState(size);
							retry = false;
						}catch(Exception exc){}
					}
				}

				Editor.this.requestFocus();
				Editor.this.repaint();
			}
		});
		JButton saveButton = new JButton("Save Level");
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(".").getCanonicalFile());
		
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(editing == null){
					JOptionPane.showMessageDialog(saveButton, "Nothing to save!");
					return;
				}
				if(editing.getPlayer() == null){
					JOptionPane.showMessageDialog(saveButton, "Level is Unplayable!");
					return;
				}
				try{
					editing.restoreSettings();
				}catch(IllegalArgumentException except){editing.setSettings(new GameSettings());}
				int returnVal = fc.showSaveDialog(saveButton);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					SaveLoad.saveGame(fc.getSelectedFile(), editing);
				}
				Editor.this.requestFocus();
			}
		});
		JButton loadButton = new JButton("Load Level");
		final JFileChooser fc2 = new JFileChooser();
		fc2.setCurrentDirectory(new File(".").getCanonicalFile());
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(editing == null)
					 editing = new GameState();
				else {
					int result = JOptionPane.showConfirmDialog(loadButton, "Are you sure you want to overwrite the current level?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
					if(result != JOptionPane.OK_OPTION)
						return;
				}
				int returnVal = fc2.showOpenDialog(loadButton);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc2.getSelectedFile();
					ObjectInputStream is = null;
					try {
					 is = new ObjectInputStream(
					 new FileInputStream(file));
					 editing.copy((GameState)is.readObject());
					}
					catch (IOException except) {editing = null;} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					finally { if (is != null)
						try {
							is.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						} 
					}
				}
				Editor.this.repaint();
			}
		});
		bottomPanel.add(newButton);
		bottomPanel.add(saveButton);
		bottomPanel.add(loadButton);
		bottomPanel.add(backButton);
		bottomPanel.add(btnCloseButton);
	}
	
	public int getInitialX(){
		return (getWidth() - getTileSize() * editing.getLabyrithm().getWidth())/2;
	}
	public int getInitialY(){
		return bottomPanel.getHeight()/2 +(getHeight() - getTileSize()* editing.getLabyrithm().getHeight())/2;
	}
	public int getTileSize(){
		int sizex = (7*getWidth()/8)/editing.getLabyrithm().getWidth();
		int sizey = (getHeight()-bottomPanel.getHeight())/editing.getLabyrithm().getHeight();
		int size = Math.min(sizex, sizey);
		return size;
	}
	public GameState getEditing(){
		return editing;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	
		this.setBackground(new Color(47,40,58));
		if(editing != null){
			MazeDraw.draw(g, editing, getInitialX(), getInitialY(), getTileSize());
		}
		
	}
}
