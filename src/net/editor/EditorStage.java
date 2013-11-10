package net.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.newdawn.slick.Color;

import net.Level;
import chu.engine.Entity;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class EditorStage extends Stage{
	
	EditorLevel level;
	
	public EditorStage() {
		super();
		loadLevel("Stairway");
	}
	
	public void beginStep() {
		for(Entity e : entities) {
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
	}
	
	public void onStep() {
		for(Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
	}
	
	public void endStep() {
		for(Entity e : entities) {
			e.endStep();
		}		
		processAddStack();
		processRemoveStack();
	}

	public EditorLevel getLevel() {
		return level;
	}

	public void loadLevel(String levelName) {
		if(levelName.equals("")) {
			level = new EditorLevel(20, 15, "New Level");
		} else {
			try {
				FileInputStream in = new FileInputStream(new File("levels/"+levelName+".lvl"));
				ObjectInputStream ois = new ObjectInputStream(in);
				level = (EditorLevel) ois.readObject();
				level.fix();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void render() {
		Color c0 = new Color(195, 239, 240);
		Color c1 = new Color(224, 248, 248);
		Renderer.drawRectangle(0, 0, 640, 480, 1.0f, c0, c0, c1, c1);
		super.render();
	}
}
