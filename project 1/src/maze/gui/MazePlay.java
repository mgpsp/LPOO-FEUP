package maze.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import maze.logic.GameState;

public class MazePlay extends MazeScreen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7276531429855866622L;
	private JPanel bottomPanel;
	private JTextField numdarts;
	private Dimension dim;
	private Point windowPos;
	private boolean shouldResize;
	private boolean shouldMove;
	private boolean firstUpdate = true;
	private GUIControl c = null;
	public MazePlay(GameState gs) throws IOException {
		super(gs);
		 c = new GUIControl(gs, this);
		JButton btnCloseButton = new JButton("Close");
		btnCloseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int result = JOptionPane.showConfirmDialog(btnCloseButton, "Are you sure you want to quit?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION)
					notifyObservers(MazeWindow.Screen.CLOSE);
				MazePlay.this.requestFocus();
				MazePlay.this.repaint();
			}
		});
		JButton btnNewGameButton = new JButton("New Game");
		btnNewGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(btnNewGameButton, "Are you sure you want to regenerate the maze?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION){
					if(!MazeRegen.requestRegen(btnNewGameButton, gs, 
							()->MazeDraw.resetCamera(gs, gs.getPlayer().getY(), gs.getPlayer().getX(), getMazeScreenHRes(),getMazeScreenVRes()),
									() -> repaint()))
						JOptionPane.showMessageDialog(btnNewGameButton, "Proccessing!");
					MazePlay.this.requestFocus();
			}}
		});
		JButton saveButton = new JButton("Save Game");
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(".").getCanonicalFile());
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showSaveDialog(saveButton);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					SaveLoad.saveGame(fc.getSelectedFile(), getGameState());
				}
				MazePlay.this.requestFocus();
			}
		});
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyObservers(MazeWindow.Screen.MAIN);
				MazePlay.this.requestFocus();
				MazePlay.this.repaint();
			}
		});
		
		numdarts = new JTextField();
		numdarts.setEditable(false);
		numdarts.setText("0");
		numdarts.setPreferredSize(new Dimension(20,20));
		numdarts.setFocusable(false);
		setLayout(new BorderLayout());
		bottomPanel = new JPanel();
		
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		bottomPanel.add(btnNewGameButton);
		bottomPanel.add(saveButton);
		bottomPanel.add(backButton);
		bottomPanel.add(btnCloseButton);
		bottomPanel.add(new JLabel("Number of darts: "));
		bottomPanel.add(numdarts);
		add(bottomPanel, BorderLayout.NORTH);
		this.addMouseListener(c);
		this.addMouseMotionListener(c);
		this.addMouseWheelListener(c);
		this.addKeyListener(c);
		
		this.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent arg0) {
				shouldMove = false;
				shouldResize = false;
				firstUpdate = true;
			}
			@Override
			public void componentMoved(ComponentEvent arg0) {}
			@Override
			public void componentResized(ComponentEvent arg0) {
				Dimension newdim = getSize();
				if(shouldResize){
					double resizex = ((double)newdim.getWidth()) / dim.getWidth();
					double resizey = ((double)newdim.getHeight()) / dim.getHeight();
					/*while(MazeDraw.getCamera().mulScale(resizex, resizey) == false && MazeDraw.getCamera().atLowerBound()){
						MazeDraw.getCamera().mulScale(1.1,1.1);
					}*/
					MazeDraw.getCamera().mulScale(resizex, resizey);
					double factor =((double)newdim.getWidth())/ ((double)newdim.getHeight()) /  MazeDraw.getCamera().getAspectRatio();
					MazeDraw.getCamera().mulScale(factor, 1);
				}else {
					
					shouldResize = true;
				}
				dim = newdim;
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				
				if(gs.getPlayer() == null)
					MazeDraw.resetCamera(gs);
				else {
					MazeDraw.resetCamera(gs, gs.getPlayer().getY(), gs.getPlayer().getX(),getMazeScreenHRes(), getMazeScreenVRes());
				}
				repaint();
				shouldResize = false;
			}
			
		});
		//MazeDraw.resetCamera(gs);
		MazeDraw.resetCamera(gs, gs.getPlayer().getY(), gs.getPlayer().getX(),getMazeScreenHRes(), getMazeScreenVRes());
		setFocusable(true);
		this.setPreferredSize(new Dimension(800,800));
		this.setDoubleBuffered(true);
		dim = getPreferredSize();
		this.setBackground(new Color(47,40,58));
		
	}
				
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	
		GameState gs = getGameState();
		updateNumdarts(gs.getPlayer().getNumDarts());
		
		if(firstUpdate){
			MazeDraw.resetCamera(gs, gs.getPlayer().getY(), gs.getPlayer().getX(),getMazeScreenHRes(), getMazeScreenVRes());
			firstUpdate = false;
		}
		MazeDraw.drawCamera2(g, gs,0,bottomPanel.getHeight(),getWidth(),getHeight());
	}
	public void updateNumdarts(int num){
		numdarts.setText(Integer.toString(num));
	}
	public int getMazeScreenVRes(){
		int height = getHeight() == 0 ? getPreferredSize().height: getHeight();
		int panelHeight = bottomPanel.getHeight()  == 0 ? bottomPanel.getPreferredSize().height: bottomPanel.getHeight();
		
		return height-panelHeight;
	}
	public int getMazeScreenHRes(){
		int width = getWidth() == 0 ? getPreferredSize().width: getWidth();
		return width;
	}

	public void windowMove(Point newPos) {
		if(this.hasFocus()){
			if(shouldMove){
				MazeDraw.getCamera().moveRelScreen((int)((newPos.getX()-windowPos.getX())*1), (int)((newPos.getY()-windowPos.getY())*1), getMazeScreenHRes(), getMazeScreenVRes());
				repaint();
			}else {
				shouldMove = true;
				
			}
			windowPos = newPos;
		}
	}
	public void startTimer(){
		c.startTimer();
	}
	public void stopTimer(){
		c.stopTimer();
	}
}
