package maze.gui;

import java.awt.Component;

import maze.logic.GameState;

public class MazeRegen {
	public static boolean generating;
	public static int runningGenThreads = 0;
	public static final int MAX_THREADS = 3;
	private static final Runnable cancelGen  = () -> generating = false;
	
	public static boolean requestRegen(Component origin, GameState gs, Runnable ... toCall){
		if(runningGenThreads>= MAX_THREADS)
			return false;
		if(!generating){
			Thread t = new Thread(){
				public void run(){
						generating = true;
						GameState newGs = new GameState();
						synchronized(gs){
							newGs.copy(gs);
						}
						newGs.regen();
						if(!isInterrupted()){
							synchronized(gs){
								gs.copy(newGs);
							}
							for(Runnable c : toCall)
								try {
									c.run();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

						}
						generating = false;
						GenerationDialog.getInstance().setVisible(false);
						runningGenThreads--;
				};
			};
			GenerationDialog.getInstance().setThread(origin, t, cancelGen);
			t.start();
			runningGenThreads++;
		}
		return true;
	}
}
