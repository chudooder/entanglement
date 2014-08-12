package net;

import java.util.ArrayList;

import net.editor.EditorLevel;
import net.editor.Element;
import net.editor.Wall;
import net.entity.GriddedEntity;
import net.entity.PlatformZone;
import net.entity.Player;
import net.grid.Block;
import net.grid.Button;
import net.grid.Exit;
import net.grid.Hat;
import net.grid.Lift;
import net.grid.LiftPlatform;
import net.grid.Milk;
import net.grid.Terrain;
import net.grid.TerrainHitbox;
import net.grid.ToggleTile;
import net.stage.EntanglementStage;
import chu.engine.Direction;
import chu.engine.anim.Camera;

/**
 * Represents an initial configuration of tiles and game objects. Also provides
 * methods to quickly check collisions.
 * 
 * @author Shawn
 * 
 */
public class Level {

	public GriddedEntity[][] grid;
	private int[][] wireGrid;

	private int height;
	private int width;
	private String name;

	public Level(int width, int height, String name) {
		grid = new GriddedEntity[height][width];
		this.height = height;
		this.width = width;
		this.name = name;
	}

	/**
	 * @return An array of TerrainHitboxes that represents an optimized version
	 *         of the list of terrain objects.
	 */
	public ArrayList<TerrainHitbox> getOptimizedTerrain(Element[][] grid) {
		ArrayList<TerrainHitbox> terrain = new ArrayList<TerrainHitbox>();
		boolean[][] alg = new boolean[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				// Look for a terrain object we haven't processed yet.
				if (!alg[i][j] && grid[i][j] != null && grid[i][j].type == 3) {
					// go as far right as we can
					int k = j;
					while (k < grid[0].length && !alg[i][k]
							&& grid[i][k] != null && grid[i][k].type == 3) {
						alg[i][k] = true;
						k++;
					}
					// k is the first x position we don't want to include
					// go down as far as we can
					int z = i + 1;
					while (z < grid.length) {
						boolean valid = true;
						for (int x = j; x < k; x++) {
							if (alg[z][x] || !(grid[z][x] != null && grid[z][x].type == 3)) {
								valid = false;
								break;
							}
						}
						if (valid) {
							for (int x = j; x < k; x++) {
								alg[z][x] = true;
							}
						} else {
							break;
						}
						z++;
					}
					// z is the first y position we don't want to include
					// create the terrainhitbox
					TerrainHitbox th = new TerrainHitbox(j * 32, i * 32,
							(k - j) * 32, (z - i) * 32);
					terrain.add(th);
				}
			}
		}
		System.out.println("Optimized terrain to " + terrain.size()
				+ " rectangles.");
		return terrain;
	}

	public String getName() {
		return name;
	}

	public int[][] getWires() {
		return wireGrid;
	}

	public boolean move(int x, int y, Direction d) {
		if (y + d.getUnitY() < 0 || y + d.getUnitY() >= height
				|| x + d.getUnitX() < 0 || x + d.getUnitX() >= width)
			return false;
		GriddedEntity e = grid[y][x];
		grid[y+d.getUnitY()][x+d.getUnitX()] = grid[y][x];
		grid[y][x] = null;
		if(e != null) {
			e.xcoord = x + d.getUnitX();
			e.ycoord = y + d.getUnitY();
		}
		return true;
	}

	public void set(int x, int y, GriddedEntity e) {
		grid[y][x] = e;
		if(e != null) {
			e.xcoord = x;
			e.ycoord = y;
		}
	}
	
	public GriddedEntity get(int x, int y) {
		if (y < 0 || y >= height || x < 0 || x >= width)
			return null;
		return grid[y][x];
	}
	
	public boolean inBounds(int x, int y) {
		return !(y < 0 || y >= height || x < 0 || x >= width);
	}
	
	/**
	 * Takes an EditorLevel and an EntanglementStage, initializes the stage with the
	 * required starting entities, and returns the Level used as a collision map.
	 * @param editorLevel
	 * @param stage
	 * @return
	 */
	public static Level setUpStage(EditorLevel editorLevel, EntanglementStage stage) {
		String n = "";
		int h = 0;
		int w = 0;
		Level level;
		h = editorLevel.height;
		w = editorLevel.width;
		n = editorLevel.name;
		level = new Level(w, h, n);
		level.wireGrid = editorLevel.wireGrid;
		Element[][] grid = editorLevel.grid;
		Terrain terrain = new Terrain(editorLevel.foreground,editorLevel.background);
		stage.addEntity(terrain);
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				int type;
				int arg;
				if (grid[i][j] == null) {
					type = -1;
					arg = -1;
				} else {
					type = grid[i][j].type;
					arg = grid[i][j].arg;
				}
				if (type == -1) {
					level.set(j, i, null);
				} else if (type == 0) {	// blocks
					Block b = new Block(j, i, arg);
					level.set(j, i, b);
					stage.addEntity(b);
				} else if (type == 1) {	// player (and camera)
					Player p = new Player(j * 32, i * 32);
					FloatyCamera c = new FloatyCamera(p, 16, 16);
					stage.setCamera(c);
					stage.addEntity(p);
				} else if (type == 2) {	// exit
					Exit e = new Exit(j, i, arg == 0);
					level.set(j, i, e);
					stage.addEntity(e);
				} else if(type == 3) {	// walls
					Wall wull =  new Wall(j, i);
					wull.stage = stage;
					level.set(j, i, wull);
				} else if (type == 4) { // 3 is terrain, which we take care of
										// later
					Button b = new Button(j * 32, i * 32, arg);
					stage.addEntity(b);
				} else if (type == 5) {	// toggle tiles
					ToggleTile tt = new ToggleTile(j, i, arg);
					level.set(j, i, tt);
					stage.addEntity(tt);
				} else if (type == 6) {	// milk
					Milk m = new Milk(j*32, i*32, arg);
					stage.addEntity(m);
				} else if (type == 7) {	// lifts
					Lift l = new Lift(j, i, arg);
					LiftPlatform platform = new LiftPlatform(j, i);
					PlatformZone pz = new PlatformZone(j*32, i*32-1, platform);
					l.platform = platform;
					stage.addEntity(platform);
					stage.addEntity(pz);
					level.set(j,i,l);
					stage.addEntity(l);
				} else if (type == 8) {	// hats
					Hat hattu = new Hat(j, i, arg);
					stage.addEntity(hattu);
				}
			}
		}
		// optimize the terrain hitboxes first
		for (TerrainHitbox th : level.getOptimizedTerrain(grid)) {
			stage.addEntity(th);
		}
		stage.level = level;
		return level;
	}

	public GriddedEntity getEntity(int x, int y) {
		return grid[y][x];
	}
}