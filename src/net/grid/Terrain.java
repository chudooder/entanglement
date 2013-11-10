package net.grid;

import net.editor.EditorLevel;
import chu.engine.Entity;
import chu.engine.anim.Tileset;

//Handles all the rendering for the background and foreground.
public class Terrain extends Entity {
	
	public static Tileset tileset;
	private int[][] background;
	private int[][] foreground;
	
	static {
		tileset = new Tileset("res/tileset.png", 32, 32);
	}
	
	public Terrain(int[][] fg, int[][] bg) {
		super(0, 0);
		background = bg;
		foreground = fg;
		renderDepth = 1.0f;
	}
	
	@Override
	public void render() {
		//Background
		for(int i=0; i<background.length; i++) {
			for(int j=0; j<background[i].length; j++) {
				if(background[i][j] != -1)
					EditorLevel.tileset.render(j*32, i*32, background[i][j]%8, background[i][j]/8, 0.8f);
			}
		}
		//Foreground
		for(int i=0; i<foreground.length; i++) {
			for(int j=0; j<foreground[i].length; j++) {
				if(foreground[i][j] != -1)
					EditorLevel.tileset.render(j*32, i*32, foreground[i][j]%8, foreground[i][j]/8, 0.5f);
			}
		}
	}

	@Override
	public void beginStep() {
		
	}

	@Override
	public void endStep() {
		
	}
}
