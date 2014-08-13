package net.grid;

import net.editor.EditorLevel;
import chu.engine.Entity;
import chu.engine.anim.Tileset;

//Handles all the rendering for the background and foreground.
public class Terrain extends Entity {
	
	public static Tileset tileset;
	private int[][][] tiles;
	
	static {
		tileset = new Tileset("res/tileset.png", 32, 32);
	}
	
	public Terrain(int[][][] tiles) {
		super(0, 0);
		this.tiles = tiles;
		renderDepth = 1.0f;
	}
	
	@Override
	public void render() {
		for(int layer = 0; layer < 5; layer ++) {
			for(int i=0; i<tiles[layer].length; i++) {
				for(int j=0; j<tiles[layer][i].length; j++) {
					if(tiles[layer][i][j] != -1)
						EditorLevel.tileset.render(j*32, i*32, tiles[layer][i][j]%8, tiles[layer][i][j]/8, 0.8f - 0.1f * layer);
				}
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
