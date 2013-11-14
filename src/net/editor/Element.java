package net.editor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Element implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1287224981830696879L;
	
	public transient static Texture BLOCK_BLUE;
	public transient static Texture BLOCK_RED;
	public transient static Texture BLOCK_YELLOW;
	public transient static Texture BLOCK;
	public transient static Texture PLAYER;
	public transient static Texture EXIT;
	public transient static Texture EXIT_CLOSED;
	public transient static Texture TERRAIN;
	public transient static Texture BUTTON_BLUE;
	public transient static Texture BUTTON_RED;
	public transient static Texture BUTTON_YELLOW;
	public transient static Texture BUTTON;
	public transient static Texture TOGGLE;
	public transient static Texture MILK_SURFACE;
	public transient static Texture LIFT;
	public transient static Texture HAT;
	public transient static ArrayList<ArrayList<Texture>> LOOKUP;
	
	public int type;
	public int arg;
	
	static {
		try {
			PLAYER = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/cookie.png"));
			EXIT = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/exit.png"));
			EXIT_CLOSED = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/exit_closed.png"));
			BLOCK_BLUE = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/block_blue.png"));
			BLOCK_RED = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/block_red.png"));
			BLOCK_YELLOW = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/block_yellow.png"));
			BLOCK = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/block.png"));
			TERRAIN = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/terrain.png"));
			BUTTON_BLUE = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/button_blue.png"));
			BUTTON_RED = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/button_red.png"));
			BUTTON_YELLOW = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/button_yellow.png"));
			BUTTON = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/button.png"));
			TOGGLE = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/toggle_tile.png"));
			MILK_SURFACE = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/milk_surface.png"));
			LIFT = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/lift_base.png"));
			HAT = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/hat.png"));
			System.out.println("Loaded editor icons");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOOKUP = new ArrayList<ArrayList<Texture>>();
		ArrayList<Texture> blocks = new ArrayList<Texture>();
		blocks.add(BLOCK_BLUE);
		blocks.add(BLOCK_RED);
		blocks.add(BLOCK_YELLOW);
		blocks.add(BLOCK);
		LOOKUP.add(blocks);
		ArrayList<Texture> players = new ArrayList<Texture>();
		players.add(PLAYER);
		LOOKUP.add(players);
		ArrayList<Texture> exits = new ArrayList<Texture>();
		exits.add(EXIT);
		exits.add(EXIT_CLOSED);
		LOOKUP.add(exits);
		ArrayList<Texture> terrains = new ArrayList<Texture>();
		terrains.add(TERRAIN);
		LOOKUP.add(terrains);
		ArrayList<Texture> buttons = new ArrayList<Texture>();
		buttons.add(BUTTON_BLUE);
		buttons.add(BUTTON_RED);
		buttons.add(BUTTON_YELLOW);
		buttons.add(BUTTON);
		LOOKUP.add(buttons);
		ArrayList<Texture> toggles = new ArrayList<Texture>();
		toggles.add(TOGGLE);
		toggles.add(TOGGLE);
		LOOKUP.add(toggles);
		ArrayList<Texture> milks = new ArrayList<Texture>();
		milks.add(MILK_SURFACE);
		LOOKUP.add(milks);
		ArrayList<Texture> lifts = new ArrayList<Texture>();
		lifts.add(LIFT);
		lifts.add(LIFT);
		lifts.add(LIFT);
		lifts.add(LIFT);
		lifts.add(LIFT);
		LOOKUP.add(lifts);
		ArrayList<Texture> hats = new ArrayList<Texture>();
		hats.add(HAT);
		LOOKUP.add(hats);
		
	}
	
	public Element(int t, int a) {
		type = t;
		arg = a;
	}
	
	public Texture getTexture() {
		return LOOKUP.get(type).get(arg);
	}
	
	public static Element getElement(int type, int arg) {
		return new Element(type, arg);
	}
	
	public static int getNumOfArgs(int type) {
		return LOOKUP.get(type).size();
	}

	public static int getNumOfTypes() {
		return LOOKUP.size();
	}
}
