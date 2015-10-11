package maze.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

public class GenerationDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1713396167611516893L;
	private Thread toStop;
	private Runnable function;
	private static GenerationDialog instance;
	public static GenerationDialog getInstance(){
		if(instance == null)
			instance = new GenerationDialog();
		return instance;
	}
	protected GenerationDialog(){
		this.setTitle("Generating");
		this.setLayout(new FlowLayout());
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		this.add(progressBar);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toStop.interrupt();
				try {
					function.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
				setVisible(false);
			}
		});
		this.setSize(200, 85);
		this.add(cancel);
		this.setVisible(false);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	void setThread(Component c, Thread t, Runnable f){
		setVisible(true);
		Point p = c.getLocationOnScreen();
		p.x +=c.getWidth()/2 - this.getWidth()/2;
		p.y +=c.getHeight()/2 - this.getHeight()/2;
		setLocation(p);
		toStop = t;
		function = f;
	}
}
