package net.stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import net.Entanglement;
import net.Settings;
import net.editor.EditorLevel;
import net.editor.Element;

import org.ini4j.Ini;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.MouseEvent;
import chu.engine.Stage;
import chu.engine.anim.Renderer;
import chu.engine.menu.MenuButton;

public class LevelSelectStage extends Stage {
	private static ArrayList<LevelEntry> levelList;
	private static final int LIST_SIZE = 15;
	private static final int Y_OFFSET = 0;
	private static Texture checkbox;
	private static Texture question_mark;
	protected static HashMap<String, LevelEntry> levels;
	private int currentLevel;
	private int offset;
	private int cheat = 0;

	static {
		try {
			checkbox = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/checkbox.png"));
			question_mark = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/question_mark.png"));
			System.out.println("Loaded level select sprites");
		} catch (IOException e) {

		}
	}

	public LevelSelectStage() {
		levelList = new ArrayList<LevelEntry>();
		levels = new HashMap<String, LevelEntry>();
		levelList.add(new LevelEntry("Beginning", 1));
		levelList.add(new LevelEntry("Push", 1));
		levelList.add(new LevelEntry("Tether", 1));
		levelList.add(new LevelEntry("Angle", 2));
		levelList.add(new LevelEntry("Stairway", 2));
		levelList.add(new LevelEntry("Drop", 3));
		levelList.add(new LevelEntry("Color", 1));
		levelList.add(new LevelEntry("Transfer", 2));
		levelList.add(new LevelEntry("Crossover", 2));
		levelList.add(new LevelEntry("Familiar", 4));
		levelList.add(new LevelEntry("Underhang", 4));
		levelList.add(new LevelEntry("Neutral", 2));
		levelList.add(new LevelEntry("Lift", 1));
		levelList.add(new LevelEntry("Elevate", 2));
		levelList.add(new LevelEntry("Halfway", 3));
		levelList.add(new LevelEntry("Delay", 3));
		levelList.add(new LevelEntry("Stack", 4));
		levelList.add(new LevelEntry("Bridge", 5));
		for (int i = 0; i < 15; i++) {
			addEntity(new LevelEntryButton(0, i * 32, 320, 32, i));
		}
		currentLevel = 0;
		offset = 0;
	}

	private class LevelEntry {
		String name;
		int difficulty;
		boolean completed;
		boolean open;
		double time;
		EditorLevel level;

		public LevelEntry(String name, int difficulty) {
			this.name = name;
			this.difficulty = difficulty;
			this.completed = false;
			this.open = false;
			this.time = -1.0;
			try {
				InputStream inputStream = ResourceLoader
						.getResourceAsStream("levels/" + name + ".lvl");
				ObjectInputStream ois = new ObjectInputStream(inputStream);
				level = (EditorLevel) ois.readObject();
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			levels.put(name, this);
		}

		public void setCompleted() {
			completed = true;
		}

		public void setOpen() {
			open = true;
		}
		
		public void recordTime(double newTime) {
			if(newTime < time || time < 0) time = newTime;
		}
	}

	private class LevelEntryButton extends MenuButton {
		int index;

		public LevelEntryButton(float x, float y, float w, float h, int i) {
			super(x, y, w, h);
			index = i;
		}

		@Override
		public void onEnter() {
			currentLevel = index + offset;
		}

		@Override
		public void onClick() {
			startLevel();
		}

		@Override
		public void onExit() {

		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public void render() {
		/* Background stuff */
		Renderer.drawRectangle(Renderer.getCamera().getScreenX(), Renderer
				.getCamera().getScreenY(),
				Renderer.getCamera().getScreenX() + 640, Renderer.getCamera()
						.getScreenY() + 480, 1.0f, Color.white);
		Renderer.drawRectangle(0, Y_OFFSET + (currentLevel - offset) * 32, 320,
				Y_OFFSET + 32 + (currentLevel - offset) * 32, 1.0f, new Color(
						35, 37, 136));
		/* Level entries */
		for (int i = offset; i < offset + LIST_SIZE; i++) {
			int y = i - offset;
			/* Checkbox */
			if (levelList.get(i).completed) {
				Renderer.render(checkbox, 0, 0, 0.5f, 1, 32, Y_OFFSET + 32 * y,
						64, Y_OFFSET + 32 + 32 * y, 0.0f);
			} else {
				Renderer.render(checkbox, 0.5f, 0, 1, 1, 32, Y_OFFSET + 32 * y,
						64, Y_OFFSET + 32 + 32 * y, 0.0f);
			}
			/* Level name */
			Color c;
			if (levelList.get(i).open) {
				if (i == currentLevel)
					c = Color.white;
				else
					c = Color.black;
			} else {
				c = new Color(220, 0, 0);
			}
			Entanglement.sourceSans24.drawString(64, Y_OFFSET + 32 * y,
					levelList.get(i).name, c);
		}
		
		/* Nifty sidebar stuff */
		LevelEntry entry = levelList.get(currentLevel);
		/* Level preview */
		if(entry.open) {
			Color[][] colors = new Color[15][20];
			Element[][] ent = levels.get(entry.name).level.grid;
			int x0 = 380;
			int y0 = 64;
			int s = 10;
			for(int i=0; i<ent.length; i++) {
				for(int j=0; j<ent[0].length; j++) {
					Color c = Color.white;
					if(ent[i][j] == null) c = Color.white;
					else if(ent[i][j].type == 7) c = new Color(100, 100, 100);
					else if(ent[i][j].type == 5) c = Color.green;
					else if(ent[i][j].type == 3) c = Color.orange;
					else if(ent[i][j].type == 2) c = new Color(200, 200, 200);
					else if(ent[i][j].type == 1) c = new Color(182, 133, 58);
					else if(ent[i][j].type == 0) {
						if(ent[i][j].arg == 0) c = Color.blue;
						else if(ent[i][j].arg == 1) c = Color.red;
						else if(ent[i][j].arg == 2) c = Color.yellow;
						else if(ent[i][j].arg == 3) c = Color.gray;
					}
					Renderer.drawRectangle(x0+j*s, y0+i*s, x0+(j+1)*s, y0+(i+1)*s, 0.0f, c);
				}
			}
			/* Best time */
			if(entry.completed) { 
				double time = entry.time;
				if(time > 0) {
					String text = String.format("%2d:%02d.%03d", (int) (time / 60000),
							(int) (time / 1000 % 60), (int) (time % 1000));
					Entanglement.sourceSans24.drawString(x0, y0+200, "Best time: "+text, Color.black);
				} else {
					Entanglement.sourceSans24.drawString(x0, y0+200, "Best time: none", Color.black);
				}
			}
		} else {
			Renderer.render(question_mark, 0, 0, 1, 1, 352, 0, 608, 256, 0.0f);
		}
	}

	@Override
	public void beginStep() {
		HashMap<Integer, Boolean> keys = Entanglement.getKeys();
		HashMap<MouseEvent, Boolean> mouseEvents = Entanglement
				.getMouseEvents();
		for (MouseEvent event : mouseEvents.keySet()) {
			if (event.dwheel < 0) {
				if (offset < levelList.size() - LIST_SIZE)
					offset++;
			} else if (event.dwheel > 0) {
				if (offset > 0)
					offset--;
			}
		}
		for (int key : keys.keySet()) {
			if (keys.get(key)) {
				if (key == Settings.getKey(Settings.K_DOWN)) {
					currentLevel++;
					if (currentLevel == levelList.size()) {
						currentLevel = 0;
						offset = 0;
					} else {
						if (currentLevel > offset + LIST_SIZE - 1) {
							offset++;
						}
					}
				} else if (key == Settings.getKey(Settings.K_UP)) {
					currentLevel--;
					if (currentLevel < 0) {
						currentLevel = levelList.size() - 1;
						offset = levelList.size() - LIST_SIZE;
					} else {
						if (currentLevel < offset) {
							offset--;
						}
					}
				} else if (key == Keyboard.KEY_RETURN) {
					startLevel();
				} else if (key == Keyboard.KEY_INSERT) {
					cheat++;
					if (cheat == 3) {
						for (LevelEntry e : levelList) {
							e.setOpen();
						}
					}
				} else if (key == Keyboard.KEY_DELETE) {
					cheat--;
					if (cheat == -10) {
						for (LevelEntry e : levelList) {
							e.open = false;
							e.completed = false;
						}
						levelList.get(0).setOpen();
					}
				} else if (key == Keyboard.KEY_ESCAPE) {
					saveData();
					Entanglement.setCurrentStage(Entanglement.mainMenu);
				}
			}
		}
		for (Entity e : entities) {
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
	}

	@Override
	public void onStep() {
		for (Entity e : entities) {
			e.onStep();
		}
	}

	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.endStep();
		}
	}

	public static EditorLevel getLevel(String name) {
		return levels.get(name).level;
	}

	private void startLevel() {
		if (levelList.get(currentLevel).open) {
			String name = levelList.get(currentLevel).name;
			boolean timing = levelList.get(currentLevel).completed;
			Entanglement.setCurrentStage(Entanglement.createStageFromLevel(
					levels.get(name).level, timing));
		}
	}

	public void completeLevel() {
		levelList.get(currentLevel).setCompleted();
		if (currentLevel < levelList.size() - 1)
			levelList.get(currentLevel + 1).setOpen();
		saveData();
	}
	
	public void completeLevel(double time) {
		levelList.get(currentLevel).setCompleted();
		levelList.get(currentLevel).recordTime(time);
		if (currentLevel < levelList.size() - 1)
			levelList.get(currentLevel + 1).setOpen();
		saveData();
	}
	
	public void saveData() {
		File file = new File("settings.ini");
		try {
			Ini ini = new Ini(file);
			for(LevelEntry entry : levelList) {
				ini.put(entry.name, "open", entry.open);
				ini.put(entry.name, "complete", entry.completed);
				ini.put(entry.name, "time", entry.time);
			}
			ini.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadData() {
		File file = new File("settings.ini");
		try {
			Ini ini = new Ini(file);
			for(LevelEntry entry : levelList) {
				entry.open = ini.get(entry.name, "open", boolean.class);
				entry.completed = ini.get(entry.name, "complete", boolean.class);
				entry.time = ini.get(entry.name, "time", double.class);
			}
			ini.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		levelList.get(0).setOpen();
	}

}
