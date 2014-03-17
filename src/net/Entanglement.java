package net;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.awt.Font;

import net.editor.EditorLevel;
import net.stage.EntanglementStage;
import net.stage.LevelSelectStage;
import net.stage.MainMenuStage;
import net.stage.RebindKeysStage;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

@SuppressWarnings("deprecation")
public class Entanglement extends Game {
	
	static Stage currentStage;
	public static LevelSelectStage selectStage;
	public static RebindKeysStage rebindKeysStage;
	public static MainMenuStage mainMenu;
	public static TrueTypeFont sourceSans72;
	public static TrueTypeFont sourceSans24;
	public static TrueTypeFont sourceSans16;
	public static void main(String[] args) {
		Entanglement game = new Entanglement();
		game.init(640, 480, "Entanglement");
		game.loop();
	}
	
	@Override
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		/* OpenGL final setup */
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		/* Font setup */
		try {
			Font awtFont = Font.createFont(Font.TRUETYPE_FONT,
					ResourceLoader.getResourceAsStream("res/SourceSansPro-Light.ttf"));
			sourceSans24 = new TrueTypeFont(awtFont.deriveFont(Font.PLAIN, 24), true);
			sourceSans16 = new TrueTypeFont(awtFont.deriveFont(Font.PLAIN, 16), true);
			System.out.println("Loaded fonts");
		} catch (Exception e) {
			System.err.println("Font not found.");
			e.printStackTrace();
		}
		selectStage = new LevelSelectStage();
		rebindKeysStage = new RebindKeysStage();
		mainMenu = new MainMenuStage();
		/* INI setup */
		Settings.readFromINI();
		selectStage.loadData();
		currentStage = mainMenu;
	}

	@Override
	public void loop() {
		while(!Display.isCloseRequested()) {
			// Get delta time
			time = System.nanoTime();
			glClear(GL_COLOR_BUFFER_BIT |
			        GL_DEPTH_BUFFER_BIT |
			        GL_STENCIL_BUFFER_BIT);
			glClearDepth(1.0f);
			getInput();
			SoundStore.get().poll(0);
			glPushMatrix();
			// Update the stage
			if(!paused) {
				currentStage.beginStep();
				currentStage.onStep();
				Renderer.getCamera().lookThrough();
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
	
	public static void setCurrentStage(Stage stage) {
		currentStage = stage;
	}
	
	public static EntanglementStage createStageFromLevel(EditorLevel level, boolean timing) {
		EntanglementStage stage = new EntanglementStage(timing);
		Level.setUpStage(level, stage);
		return stage;
	}

}
