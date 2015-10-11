package maze.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import maze.logic.GameSettings;
import maze.logic.GameState;
import maze.logic.Dragon.DragonBehaviour;

public class SettingsMenu extends MazeScreen {

	private static final long serialVersionUID = -1484941279603198157L;
	
	private BindingButton toEdit;
	private JPanel bindings;
	private JSlider dragonSlider;
	private JSlider quiverSlider;
	private JSpinner sizeSlider;
	private ButtonGroup behaviours;
	
	public SettingsMenu(GameState g) throws IOException {
		super(g);
		addSliders();
		addBehaviours();
		addBindingButtons();
		addBottomButtons();
		this.setPreferredSize(new Dimension(300,500));
		setFocusable(true);
	}
	
	private void addSliders() {
		JPanel sizePanel = new JPanel();
		sizePanel.setBorder(new TitledBorder(null, "Size of Maze",TitledBorder.LEFT,TitledBorder.TOP));
		sizeSlider = new JSpinner();
		sizeSlider.setValue(getGameState().getSettings().getMazeHeight());
		sizeSlider.setPreferredSize(new Dimension(50,sizeSlider.getPreferredSize().height));
		sizePanel.add(sizeSlider);
		sizePanel.setPreferredSize(new Dimension(100,50));
		add(sizePanel);
		
		
		JPanel quiverPanel = new JPanel();
		quiverPanel.setBorder(new TitledBorder(null, "Number of quivers",TitledBorder.RIGHT,TitledBorder.TOP));
		quiverSlider = new JSlider(JSlider.HORIZONTAL, 0, 2*GameSettings.maxDragons((Integer)sizeSlider.getValue()), getGameState().getSettings().getNumquivers());
		
		quiverSlider.setPaintTicks(true);
		quiverSlider.setSnapToTicks(true);
		int newnum1 = GameSettings.maxDragons((Integer)sizeSlider.getValue());
		int quiverspacing = Math.max(2*newnum1 / 10,1);
		quiverSlider.setMajorTickSpacing(quiverspacing);
		quiverSlider.setLabelTable(quiverSlider.createStandardLabels(quiverspacing));
		
		quiverSlider.setPaintLabels(true);
		quiverSlider.setMinorTickSpacing(1);
		quiverPanel.add(quiverSlider);
		add(quiverPanel);
		quiverSlider.setValue(getGameState().getSettings().getNumquivers());
		
		JPanel dragonPanel = new JPanel();
		dragonPanel.setBorder(new TitledBorder(null, "Number of Dragons",TitledBorder.LEFT,TitledBorder.TOP));
		dragonSlider = new JSlider(JSlider.HORIZONTAL, 1, GameSettings.maxDragons((Integer)sizeSlider.getValue()), getGameState().getSettings().getNumdragons());
		dragonSlider.setPaintTicks(true);
		dragonSlider.setSnapToTicks(true);
		
		int dragonspacing1 = Math.max(newnum1 / 10,1);
		dragonSlider.setMajorTickSpacing(dragonspacing1);
		dragonSlider.setLabelTable(dragonSlider.createStandardLabels(dragonspacing1));
		
		dragonSlider.setMinorTickSpacing(1);
		dragonSlider.setPaintLabels(true);
		dragonPanel.add(dragonSlider);
		add(dragonPanel);
		dragonSlider.setValue(getGameState().getSettings().getNumdragons());
		
		sizeSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if((Integer)sizeSlider.getValue() > 686)
					sizeSlider.setValue(686);
				if((Integer)sizeSlider.getValue() < 7){
					sizeSlider.setValue(7);
				}
				int newnum = GameSettings.maxDragons((Integer)sizeSlider.getValue());
				if(dragonSlider.getValue() > newnum)
					dragonSlider.setValue(newnum);
				dragonSlider.setMaximum(newnum);
				int dragonspacing = Math.max(newnum / 10,1);
				dragonSlider.setMajorTickSpacing(dragonspacing);
				dragonSlider.setLabelTable(dragonSlider.createStandardLabels(dragonspacing));
				
				if(quiverSlider.getValue() > 2*newnum)
					quiverSlider.setValue(2*newnum);
				quiverSlider.setMaximum(2*newnum);
				int quiverspacing = Math.max(2*newnum / 10,1);
				quiverSlider.setMajorTickSpacing(quiverspacing);
				quiverSlider.setLabelTable(quiverSlider.createStandardLabels(quiverspacing));
			}	
		});
	}
	
	private void addBehaviours() {
		JPanel behaviourPanel = new JPanel();
		behaviourPanel.setBorder(new TitledBorder(null, "Dragon Behaviour",TitledBorder.CENTER,TitledBorder.TOP));
		behaviours = new ButtonGroup();
		JRadioButton stationary = new JRadioButton("Stationary");
		JRadioButton moving = new JRadioButton("Moving");
		JRadioButton sleeping = new JRadioButton("Sleeping");
		behaviours.add(stationary);
		behaviours.add(moving);
		behaviours.add(sleeping);
		
		behaviourPanel.add(stationary);
		behaviourPanel.add(moving);
		behaviourPanel.add(sleeping);
		
		add(behaviourPanel);
		
		stationary.setActionCommand(stationary.getText());
		moving.setActionCommand(moving.getText());
		sleeping.setActionCommand(sleeping.getText());
		sleeping.setSelected(true);
	}
	
	private void addBindingButtons() {
		bindings = new JPanel();
		bindings.setBorder(new TitledBorder("Key Bindings"));
		bindings.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 15, 0, 0);
		BindingButton upBind = new BindingButton("Up", ()-> {return GUIControl.getUpButton();},(i) ->{return GUIControl.setUpButton(i);});
		bindings.add(upBind, c);
		ActionListener a = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() instanceof BindingButton){
					if(toEdit == arg0.getSource())
						toEdit = null;
					else {
						toEdit = (BindingButton) arg0.getSource();
					}
					SettingsMenu.this.requestFocus();
				}
			}
			
		};
		upBind.addActionListener(a);
		c.gridx = 0;
		c.gridy = 1;
		BindingButton leftBind = new BindingButton("Left", ()-> {return GUIControl.getLeftButton();},(i) ->{return GUIControl.setLeftButton(i);});
		bindings.add(leftBind,c);
		leftBind.addActionListener(a);
		c.insets = new Insets(0, 0, 0, 15);
		c.gridx = 1;
		c.gridy = 0;
		BindingButton downBind = new BindingButton("Down", ()-> {return GUIControl.getDownButton();},(i) ->{return GUIControl.setDownButton(i);});
		bindings.add(downBind,c);
		downBind.addActionListener(a);
		
		c.gridx = 1;
		c.gridy = 1;
		BindingButton rightBind = new BindingButton("Right", ()-> {return GUIControl.getRightButton();},(i) ->{return GUIControl.setRightButton(i);});
		bindings.add(rightBind,c);
		rightBind.addActionListener(a);
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 3;
		BindingButton throwBind = new BindingButton("Throw", ()-> {return GUIControl.getThrowButton();},(i) ->{return GUIControl.setThrowButton(i);});
		bindings.add(throwBind,c);
		throwBind.addActionListener(a);
		
		add(bindings);
		this.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(toEdit == null)
					return;
				toEdit.setBinding(arg0.getKeyCode());
				toEdit = null;
				for(Component c : bindings.getComponents()){
					if(c instanceof BindingButton)
						((BindingButton) c).updateText();
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
	}
	
	private void addBottomButtons() {
		JPanel bottom = new JPanel();
		JButton saveButton = new JButton("Save");
		bottom.add(saveButton);
		saveButton.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				String result = behaviours.getSelection().getActionCommand();
				DragonBehaviour behaviour;
				if(result.equals("Stationary"))
					behaviour = DragonBehaviour.STATIONARY;
				else if(result.equals("Moving"))
					behaviour = DragonBehaviour.MOVING;
				else behaviour = DragonBehaviour.SLEEPING;
				try{
					getGameState().getSettings().setAttributes(new GameSettings(dragonSlider.getValue(), quiverSlider.getValue(),(Integer) sizeSlider.getValue(), (Integer)sizeSlider.getValue(), behaviour));
				}catch(Exception e1){}
				GUIControl.saveKeys();
				for(Component c : bindings.getComponents()){
					if(c instanceof BindingButton)
						((BindingButton) c).updateText();
				}
			}
		});
		JButton backButton = new JButton("Back");
		bottom.add(backButton);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIControl.restoreKeys();
				notifyObservers(MazeWindow.Screen.MAIN);
				for(Component c : bindings.getComponents()){
					if(c instanceof BindingButton)
						((BindingButton) c).updateText();
				}
			}
		});
		add(bottom, BorderLayout.SOUTH);
	}
}
