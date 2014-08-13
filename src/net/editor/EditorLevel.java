package net.editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;

public class EditorLevel implements Serializable {
	
	/**
	 * i don't know why there was a comment block here but it's gonna stay there
	 */
	private static final long serialVersionUID = 8978961010427430449L;
	private static final int NUM_LAYERS = 5;
	private static transient Texture wireTex;
	private static transient Texture wireTexBlue;
	public static transient Tileset tileset;
	public int[][] wireGrid;
	public int[][][] tiles;
	public Element[][][] entities;
	public int width;
	public int height;
	public String name;
	
	static {
		try {
			wireTex = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/wire.png"));
			wireTexBlue = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/wire_blue.png"));
			tileset = new Tileset("res/tileset.png", 32, 32);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public EditorLevel(int w, int h, String n) {
		width = w;
		height = h;
		name = n;
		wireGrid = new int[height][width];
		tiles = new int[NUM_LAYERS][height][width];
		for(int i=0; i<NUM_LAYERS; i++)
			for(int j=0; j<height; j++)
				Arrays.fill(tiles[i][j], -1);
		entities = new Element[NUM_LAYERS][height][width];
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				wireGrid[i][j] = 0;
			}
		}
	}
	
	public void setTile(int layer, int gridX, int gridY, int index) {
		if(gridX < 0 || gridY < 0 || gridX >= width || gridY >= height)
			return;
		tiles[layer][gridY][gridX] = index;
	}

	public void setElement(int layer, int gridX, int gridY, int type, int arg) {
		if(gridX < 0 || gridY < 0 || gridX >= width || gridY >= height)
			return;
		if(type == -1) {
			entities[layer][gridY][gridX] = null;
			return;
		}
		entities[layer][gridY][gridX] = new Element(type, arg);
	}
	
	public void setWire(int gridX, int gridY, int arg) {
		if(gridX < 0 || gridY < 0 || gridX >= width || gridY >= height)
			return;
		wireGrid[gridY][gridX] = arg;
	}
	
	public void serialize() {
		File file = new File("levels/"+name+".lvl");
		FileOutputStream fo;
		ObjectOutputStream oos;
		try {
			fo = new FileOutputStream(file);
			oos = new ObjectOutputStream(fo);
			oos.writeObject(this);
			oos.close();
			System.out.println("Level serialization successful.");
		} catch (FileNotFoundException e) {
			System.out.println("Invalid file path!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Failed to create object output stream");
			e.printStackTrace();
		}
	}

	public void fix() {
		
	}

	public void render(boolean[] visibility) {
		//Tiles and entities
		for(int layer=0; layer<NUM_LAYERS; layer++) {
			if(!visibility[layer]) continue;
			for(int i=0; i<tiles[layer].length; i++) {
				for(int j=0; j<tiles[layer][i].length; j++) {
					if(tiles[layer][i][j] != -1)
						EditorLevel.tileset.render(j*32, i*32, tiles[layer][i][j]%8, tiles[layer][i][j]/8, 0.4f - 0.01f*layer);
					if(entities[layer][i][j] != null) {
						float tx = 32.0f/entities[layer][i][j].getTexture().getImageWidth();
						Renderer.render(entities[layer][i][j].getTexture(), 0, 0, tx, 1,
								j * 32, i * 32, j * 32 + 32, i * 32 + 32, 0.4f - 0.01f * layer);
					}
				}
			}
		}
		
		//Wires
		for(int i=0; i<wireGrid.length; i++) {
			for(int j=0; j<wireGrid[i].length; j++) {
				if(wireGrid[i][j] == 1)
					Renderer.render(wireTex, 0, 0, 1, 1, j*32, i*32, j*32+32, i*32+32, 0.3f);
				if(wireGrid[i][j] == 2)
					Renderer.render(wireTexBlue, 0, 0, 1, 1, j*32, i*32, j*32+32, i*32+32, 0.3f);
			}
		}
	}

	public void resize(int dx, int dy) {
		if(width+dx < 20 || height+dy < 15) return;
		width += dx;
		height += dy;
		// copy the level data into new-sized arrays
		int minX = Math.min(width, tiles[0][0].length);
		int minY = Math.min(height, tiles[0].length);
		// tile data first
		int[][][] newTiles = new int[NUM_LAYERS][height][width];
		for(int i=0; i<NUM_LAYERS; i++)
			for(int j=0; j<newTiles[i].length; j++)
				Arrays.fill(newTiles[i][j], -1);
		for(int i=0; i<NUM_LAYERS; i++)
			for(int j=0; j<minY; j++)
				for(int k=0; k<minX; k++)
					newTiles[i][j][k] = tiles[i][j][k];
		// entities next
		Element[][][] newEntities = new Element[NUM_LAYERS][height][width];
		for(int i=0; i<NUM_LAYERS; i++)
			for(int j=0; j<minY; j++)
				for(int k=0; k<minX; k++)
					newEntities[i][j][k] = entities[i][j][k];
		// wires last
		int[][] newWireGrid = new int[height][width];
		for(int i=0; i<minY; i++) {
			for(int j=0; j<minX; j++) {
				newWireGrid[i][j] = wireGrid[i][j];
			}
		}
		// then set the original fields to the new ones
		wireGrid = newWireGrid;
		entities = newEntities;
		tiles = newTiles;
		System.out.println("Level resized from "+(width-dx)+"x"+(height-dy)+" to "+width+"x"+height);
	}

}
