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
import net.grid.Lift;
import net.grid.LiftPlatform;
import net.grid.Milk;
import net.grid.Terrain;
import net.grid.TerrainHitbox;
import net.grid.ToggleTile;
import net.stage.EntanglementStage;
import chu.engine.Direction;

/**
 * Represents an initial configuration of tiles and game objects. Also provides
 * methods to quickly check collisions.
 * 
 * @author Shawn
 * 
 */
public class Level {

	private GriddedEntity[][] grid;
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
		// 0 = right, 1 = up, 2 = left, 3 = down
		GriddedEntity temp = grid[y][x];
		if (!(temp instanceof Block))
			return false;
		if (y + d.getUnitY() < 0 || y + d.getUnitY() > height
				|| x + d.getUnitX() < 0 || x + d.getUnitX() > width)
			return false;
		if (grid[y + d.getUnitY()][x + d.getUnitX()] == null) {
			grid[y + d.getUnitY()][x + d.getUnitX()] = temp;
			grid[y][x] = null;
			temp.xcoord += d.getUnitX();
			temp.ycoord += d.getUnitY();
			return true;
		} else {
			if (grid[y + d.getUnitY()][x + d.getUnitX()].move(d)) {
				grid[y + d.getUnitY()][x + d.getUnitX()] = temp;
				grid[y][x] = null;
				temp.xcoord += d.getUnitX();
				temp.ycoord += d.getUnitY();
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean testMove(int x, int y, Direction d) {
		GriddedEntity temp = grid[y][x];
		if (!(temp instanceof Block))
			return false;
		if (y + d.getUnitY() < 0 || y + d.getUnitY() >= height
				|| x + d.getUnitX() < 0 || x + d.getUnitX() >= width)
			return false;
		if (grid[y + d.getUnitY()][x + d.getUnitX()] == null) {
			return true;
		} else {
			if (testMove(x + d.getUnitX(), y + d.getUnitY(), d)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void set(int x, int y, GriddedEntity e) {
		grid[y][x] = e;
		if(e != null) {
			e.xcoord = x;
			e.ycoord = y;
		}
	}

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
				} else if (type == 0) {
					Block b = new Block(j, i, arg);
					level.set(j, i, b);
					stage.addEntity(b);
				} else if (type == 1) {
					Player p = new Player(j * 32, i * 32);
					stage.addEntity(p);
				} else if (type == 2) {
					Exit e = new Exit(j, i, arg == 0);
					level.set(j, i, e);
					stage.addEntity(e);
				} else if(type == 3) {
					Wall wull =  new Wall(j, i);
					wull.stage = stage;
					level.set(j, i, wull);
				} else if (type == 4) { // 3 is terrain, which we take care of
										// later
					Button b = new Button(j * 32, i * 32, arg);
					stage.addEntity(b);
				} else if (type == 5) {
					ToggleTile tt = new ToggleTile(j, i, arg);
					level.set(j, i, tt);
					stage.addEntity(tt);
				} else if (type == 6) {
					Milk m = new Milk(j*32, i*32, arg);
					stage.addEntity(m);
				} else if (type == 7) {
					Lift l = new Lift(j, i, arg);
					LiftPlatform platform = new LiftPlatform(j, i);
					PlatformZone pz = new PlatformZone(j*32, i*32-1, platform);
					l.platform = platform;
					stage.addEntity(platform);
					stage.addEntity(pz);
					level.set(j,i,l);
					stage.addEntity(l);
				}
			}
		}
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