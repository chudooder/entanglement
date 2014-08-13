package net.editor;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.awt.Font;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.SoundStore;

import chu.engine.Game;

@SuppressWarnings("deprecation")
/**
 * i can make levels now
 * @author Shawn
 *
 */
public class Editor extends Game {
	
	EditorStage currentStage;
	static TrueTypeFont messageFont;
	
	public static void main(String[] args) {
		Editor game = new Editor();
		game.init(640, 480, "Entanglement Level Editor");
		game.loop();
	}
	
	@Override
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		currentStage = new EditorStage();
		EditorMenu menu = new EditorMenu(0, 0);
		currentStage.addEntity(menu);
		menu.setLevelName(currentStage.getLevel().name);
		try {
			Font awtFont = new Font("Open Sans", Font.PLAIN, 12);
			messageFont = new TrueTypeFont(awtFont, false);
		} catch (Exception e) {
			System.err.println("Font not found.");
			e.printStackTrace();
		}
	}

	@Override
	public void loop() {
		while(!Display.isCloseRequested()) {
			time = System.nanoTime();
			glClear(GL_COLOR_BUFFER_BIT |
			        GL_DEPTH_BUFFER_BIT |
			        GL_STENCIL_BUFFER_BIT);
			glClearDepth(1.0f);
			getInput();
			SoundStore.get().poll(0);
			glPushMatrix();
			if(!paused) {
				currentStage.beginStep();
				currentStage.onStep();
				currentStage.getCamera().lookThrough();
				currentStage.render();
				currentStage.endStep();
			}
			glPopMatrix();
			Display.update();
			timeDelta = System.nanoTime()-time;
		}
		AL.destroy();
		Display.destroy();
	}

}
