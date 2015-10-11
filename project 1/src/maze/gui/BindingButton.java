package maze.gui;

import java.awt.event.KeyEvent;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import javax.swing.JButton;

public class BindingButton extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3199410473998953025L;
	private Supplier<Integer>buttonGetter;
	private IntFunction<Boolean> buttonChanger;
	BindingButton(String name, Supplier<Integer>buttonGetter, IntFunction<Boolean>buttonChanger){
		super();
		setName(name);
		this.buttonGetter = buttonGetter;
		this.buttonChanger = buttonChanger;
		updateText();
	}
	public int getBinding(){
		return buttonGetter.get();
	}
	public boolean setBinding(int i){
		return buttonChanger.apply(i);
	}
	public void updateText(){
		setText(getName() + ": " + KeyEvent.getKeyText(getBinding()));
	}
}
