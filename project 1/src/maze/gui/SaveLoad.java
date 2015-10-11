package maze.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import maze.logic.GameState;

public class SaveLoad {
	public static boolean saveGame(File f,GameState g){
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(new FileOutputStream(f));
			os.writeObject(g);
		}
		catch (IOException except) { }
		finally { 
			if (os != null)
				try {
					os.close();
				} 
			catch (IOException e1) {
				e1.printStackTrace();
				return false;
			} 
		}
		return true;
	}
	public static boolean loadGame(File f, GameState g){
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(
					new FileInputStream(f));
			if(g == null)
				g = new GameState();
			g.copy((GameState) is.readObject());
		}
		catch (IOException except) {  } catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		finally { 
			if (is != null)
				try {
					is.close();
				} 
			catch (IOException e1) {
				e1.printStackTrace();
				return false;
			} 
		}
		return true;
	}
	public static boolean saveKeyBindings(File f){
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(new FileOutputStream(f));
			GUIControl.storeKeys(os);
		}
		catch (IOException except) { }
		finally { 
			if (os != null)
				try {
					os.close();
				} 
			catch (IOException e1) {
				e1.printStackTrace();
				return false;
			} 
		}
		return true;
	}
	public static boolean loadKeyBindings(File f){
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(
					new FileInputStream(f));
			GUIControl.retrieveKeys(is);
		}
		catch (IOException except) {  } catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		finally { 
			if (is != null)
				try {
					is.close();
				} 
			catch (IOException e1) {
				e1.printStackTrace();
				return false;
			} 
		}
		return true;
	}
}
