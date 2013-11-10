package net.stage;

import java.io.IOException;
import java.util.HashMap;

import net.Entanglement;
import net.Settings;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.Stage;
import chu.engine.anim.Renderer;
import chu.engine.menu.MenuButton;

public class RebindKeysStage extends Stage {

	private static Texture box;
	private static Texture box_selected;
	private static Texture box_hover;
	private static int editing;
	private static int hover;

	static {
		try {
			box = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/key_rebind_box.png"));
			box_selected = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/key_rebind_box_selected.png"));
			box_hover = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/key_rebind_box_hover.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RebindKeysStage() {
		super();
		editing = -1;
		hover = -1;
		// Set up menu buttons
		for(int i=0; i<10; i++) {
			addEntity(new RebindKeyButton(250, 48+36*i, 64, 32, i));
		}
		addEntity(new DoneButton(250, 440, 64, 32));
	}

	@Override
	public void beginStep() {
		for(Entity e : entities) {
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
		HashMap<Integer, Boolean> keys = Entanglement.getKeys();
		for(int key : keys.keySet()) {
			if(keys.get(key)) {
				if(editing != -1) {
					Settings.rebind(editing, key);
					editing = -1;
				}
			}
		}
	}

	@Override
	public void onStep() {
		for(Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
	}

	@Override
	public void endStep() {
		for(Entity e : entities) {
			e.endStep();
		}
		processAddStack();
		processRemoveStack();
	}

	@Override
	public void render() {
		Renderer.drawRectangle(Renderer.getCamera().getScreenX(), Renderer
				.getCamera().getScreenY(),
				Renderer.getCamera().getScreenX() + 640, Renderer.getCamera()
						.getScreenY() + 480, 1.0f, Color.white);
		String[] names = { "Left", "Right", "Up", "Down", "Jump", "Kick",
				"Blue tether", "Orange tether", "Cancel tether", "Reset" };
		int x0 = 250;
		int y0 = 48;
		for (int i = 0; i < 10; i++) {
			if (editing == i) {
				Renderer.render(box_selected, 0, 0, 1, 1, x0, y0 + 36 * i, x0 + 64, y0
						+ 32 + 36 * i, 0.9f);
			} else if (hover == i) {
				Renderer.render(box_hover, 0, 0, 1, 1, x0, y0 + 36 * i,
						x0 + 64, y0 + 32 + 36 * i, 0.9f);
			} else {
				Renderer.render(box, 0, 0, 1, 1, x0, y0 + 36 * i,
						x0 + 64, y0 + 32 + 36 * i, 0.9f);
			}
			String keyname = Keyboard.getKeyName(Settings.getKey(i));
			int width = Entanglement.sourceSans16.getWidth(keyname);
			Entanglement.sourceSans16.drawString(x0 + 32 - width / 2, y0 + 6 + 36
					* i, keyname, Color.black);
			Entanglement.sourceSans16.drawString(x0 + 100, y0 + 6 + 36 * i,
					names[i], Color.black);
		}
		super.render();
	}
	
	private class RebindKeyButton extends MenuButton {
		
		int index;

		public RebindKeyButton(float x, float y, float w, float h, int i) {
			super(x, y, w, h);
			index = i;
		}

		@Override
		public void onEnter() {
			hover = index;
		}

		@Override
		public void onClick() {
			editing = index;
		}

		@Override
		public void onExit() {
			hover = -1;
		}
		
	}
	
	private class DoneButton extends MenuButton {

		public DoneButton(float x, float y, float w, float h) {
			super(x, y, w, h);
			sprite.addAnimation("HOVER", box_hover);
			sprite.addAnimation("DEFAULT", box);
			renderDepth = 0.0f;
		}

		@Override
		public void onEnter() {
			sprite.setAnimation("HOVER");
		}

		@Override
		public void onClick() {
			Settings.saveToINI();
			Entanglement.setCurrentStage(Entanglement.mainMenu);
		}

		@Override
		public void onExit() {
			sprite.setAnimation("DEFAULT");
		}
		
		@Override
		public void render() {
			super.render();
			Entanglement.sourceSans16.drawString(x+14, y+6, "Done", Color.black);
		}
	}

}
