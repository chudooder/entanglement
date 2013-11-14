package net;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.Ini;

public class Settings {

	
	public static int K_LEFT = 0;
	public static int K_RIGHT = 1; 
	public static int K_UP = 2;
	public static int K_DOWN = 3;
	public static int K_JUMP = 4;
	public static int K_KICK = 5;
	public static int K_FIRE1 = 6;
	public static int K_FIRE2 = 7;
	public static int K_CLEAR = 8;
	public static int K_RESET = 9;
	private static Map<Integer, Integer> CONTROLS = new HashMap<Integer, Integer>();
	private static Map<String, Boolean> HATS = new HashMap<String, Boolean>();
	public static int HATNUM = 0;
	
	public static void readFromINI() {
		File file = new File("settings.ini");
		if(!file.exists()) {
			CONTROLS.put(0, 30);
			CONTROLS.put(1, 32);
			CONTROLS.put(2, 17);
			CONTROLS.put(3, 31);
			CONTROLS.put(4, 17);
			CONTROLS.put(5, 57);
			CONTROLS.put(6, 36);
			CONTROLS.put(7, 38);
			CONTROLS.put(8, 37);
			CONTROLS.put(9, 19);
			saveToINI();
		} else {
			try {
				Ini ini = new Ini(file);
				CONTROLS.put(0, ini.get("Controls", "LEFT", int.class));
				CONTROLS.put(1, ini.get("Controls", "RIGHT", int.class));
				CONTROLS.put(2, ini.get("Controls", "UP", int.class));
				CONTROLS.put(3, ini.get("Controls", "DOWN", int.class));
				CONTROLS.put(4, ini.get("Controls", "JUMP", int.class));
				CONTROLS.put(5, ini.get("Controls", "KICK", int.class));
				CONTROLS.put(6, ini.get("Controls", "FIRE1", int.class));
				CONTROLS.put(7, ini.get("Controls", "FIRE2", int.class));
				CONTROLS.put(8, ini.get("Controls", "CLEAR", int.class));
				CONTROLS.put(9, ini.get("Controls", "RESET", int.class));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void saveToINI() {
		File file = new File("settings.ini");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try {
			Ini ini = new Ini(file);
			ini.put("Controls", "LEFT", CONTROLS.get(0));
			ini.put("Controls", "RIGHT", CONTROLS.get(1));
			ini.put("Controls", "UP", CONTROLS.get(2));
			ini.put("Controls", "DOWN", CONTROLS.get(3));
			ini.put("Controls", "JUMP", CONTROLS.get(4));
			ini.put("Controls", "KICK", CONTROLS.get(5));
			ini.put("Controls", "FIRE1", CONTROLS.get(6));
			ini.put("Controls", "FIRE2", CONTROLS.get(7));
			ini.put("Controls", "CLEAR", CONTROLS.get(8));
			ini.put("Controls", "RESET", CONTROLS.get(9));
			ini.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int getKey(int control) {
		return CONTROLS.get(control);
	}

	public static void rebind(int control, int key) {
		CONTROLS.put(control, key);
	}
	
	public static void gotHat(String levelName) {
		if(HATS.containsKey(levelName)) {
			return;
		} else {
			HATS.put(levelName, true);
			HATNUM++;
		}
	}
}
