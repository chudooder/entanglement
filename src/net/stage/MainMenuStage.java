package net.stage;

import java.io.IOException;

import net.Entanglement;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.Stage;
import chu.engine.anim.Renderer;
import chu.engine.menu.MenuButton;

/**
 * man documenting stuff sucks
 * this is the main menu btw
 * @author Shawn
 *
 */
public class MainMenuStage extends Stage {
	
	private static Texture bg;
	private static Texture title;
	
	static {
		try {
			bg = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/title_background.png"));
			title = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/title.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public MainMenuStage() {
		super();
		addEntity(new PlayButton(-20, 286, 256, 32));
		addEntity(new OptionsButton(-20, 318, 256, 32));
	}

	@Override
	public void beginStep() {
		for(Entity e : entities) {
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
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
		// Background
		Color c0 = new Color(195, 239, 240);
		Color c1 = new Color(224, 248, 248);
		Renderer.drawRectangle(0, 0, 640, 480, 1.0f, c0, c0, c1, c1);
		Renderer.render(title, 0, 0, 1, 1, 64, 64, 64+512, 64+128, 0.0f);
		Renderer.render(bg, 0, 0, 1, 1, 640-512, 480-256, 640, 480, 0.0f);
		super.render();
	}
	
	/* Private classes */
	
	private class PlayButton extends MenuButton {
		Texture texture;
		public PlayButton(float x, float y, float w, float h) {
			super(x, y, w, h);
			try {
				texture = TextureLoader.getTexture("PNG",
						ResourceLoader.getResourceAsStream("res/play_button.png"));
				System.out.println("Loaded main menu sprites");
			} catch (IOException e) {
				e.printStackTrace();
			}
			sprite.addAnimation("DEFAULT", texture);
		}

		@Override
		public void onEnter() {
			x += 20;
		}
		@Override
		public void onClick() {
			Entanglement.setCurrentStage(Entanglement.selectStage);
		}
		@Override
		public void onExit() {
			x -= 20;
		}
	}
	
	private class OptionsButton extends MenuButton {
		Texture texture;
		public OptionsButton(float x, float y, float w, float h) {
			super(x, y, w, h);
			try {
				texture = TextureLoader.getTexture("PNG",
						ResourceLoader.getResourceAsStream("res/options_button.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			sprite.addAnimation("DEFAULT", texture);
		}

		@Override
		public void onEnter() {
			x += 20;
		}
		@Override
		public void onClick() {
			Entanglement.setCurrentStage(Entanglement.rebindKeysStage);
		}
		@Override
		public void onExit() {
			x -= 20;
		}
	}

}
