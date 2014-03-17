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
	private static transient Texture wireTex;
	private static transient Texture wireTexBlue;
	public static transient Tileset tileset;
	public Element[][] grid;
	public int[][] wireGrid;
	public int[][] foreground;
	public int[][] background;
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
		grid = new Element[height][width];
		wireGrid = new int[height][width];
		foreground = new int[height][width];
		for(int[] row : foreground)
			Arrays.fill(row, -1);
		background = new int[height][width];
		for(int[] row : background)
			Arrays.fill(row, -1);
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				wireGrid[i][j] = 0;
			}
		}
	}
	
	public void renderEntities() {
		//Entities
		for(int i=0; i<grid.length; i++) {
			for(int j=0; j<grid[i].length; j++) {
				if(grid[i][j] != null) {
					float tx = 32.0f/grid[i][j].getTexture().getImageWidth();
					Renderer.render(grid[i][j].getTexture(), 0, 0, tx, 1,
							j * 32, i * 32, j * 32 + 32, i * 32 + 32, 0.5f);
				}
			}
		}
	}
	public void renderWires() {
		//Wires
		for(int i=0; i<grid.length; i++) {
			for(int j=0; j<grid[i].length; j++) {
				if(wireGrid[i][j] == 1)
					Renderer.render(wireTex, 0, 0, 1, 1, j*32, i*32, j*32+32, i*32+32, 0.3f);
				if(wireGrid[i][j] == 2)
					Renderer.render(wireTexBlue, 0, 0, 1, 1, j*32, i*32, j*32+32, i*32+32, 0.3f);
			}
		}
	}
	public void renderBackground() {
		//Background
		for(int i=0; i<grid.length; i++) {
			for(int j=0; j<grid[i].length; j++) {
				if(background[i][j] != -1)
					EditorLevel.tileset.render(j*32, i*32, background[i][j]%8, background[i][j]/8, 0.6f);
			}
		}
	}
	public void renderForeground() {
		//Foreground
		for(int i=0; i<grid.length; i++) {
			for(int j=0; j<grid[i].length; j++) {
				if(foreground[i][j] != -1)
					EditorLevel.tileset.render(j*32, i*32, foreground[i][j]%8, foreground[i][j]/8, 0.4f);
			}
		}
	}
	
	public void setBackground(int gridX, int gridY, int index) {
		if(gridX < 0 || gridY < 0 || gridX >= width || gridY >= height)
			return;
		background[gridY][gridX] = index;
	}
	
	public void setForeground(int gridX, int gridY, int index) {
		if(gridX < 0 || gridY < 0 || gridX >= width || gridY >= height)
			return;
		foreground[gridY][gridX] = index;
	}

	public void setElement(int gridX, int gridY, int type, int arg) {
		if(gridX < 0 || gridY < 0 || gridX >= width || gridY >= height)
			return;
		if(type == -1) {
			grid[gridY][gridX] = null;
			return;
		}
		grid[gridY][gridX] = new Element(type, arg);
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
		if(grid == null) grid = new Element[height][width];
		if(wireGrid == null) wireGrid = new int[height][width];
		if(foreground == null) foreground = new int[height][width];
		if(background == null) background = new int[height][width];
	}

}
