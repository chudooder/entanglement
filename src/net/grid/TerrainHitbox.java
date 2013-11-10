package net.grid;

import chu.engine.Entity;
import chu.engine.RectangleHitbox;

/**
 * Hitboxes for the terrain that can be of any rectangular size.
 * These are generated at the start of the level, and serve to
 * reduce the amount of collision checks between the player and
 * solid surfaces.
 * 
 * The generation algorithm is not necessarily the best possible
 * solution, but it should be pretty good.
 * @author Shawn
 *
 */
public class TerrainHitbox extends Entity {
	
	public TerrainHitbox(float x, float y, int width, int height) {
		super(x, y);
		solid = true;
		this.width = width;
		this.height = height;
		hitbox = new RectangleHitbox(this, 0, 0, width, height);
	}

	@Override
	public void beginStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub
		
	}
	
	public void render() {
//		Renderer.drawLine(x, y, x+width, y, 1, 0.0f, Color.red, Color.red);
//		Renderer.drawLine(x+width, y, x+width, y+height, 1, 0.0f, Color.red, Color.red);
//		Renderer.drawLine(x+width, y+height, x, y+height, 1, 0.0f, Color.red, Color.red);
//		Renderer.drawLine(x, y+height, x, y, 1, 0.0f, Color.red, Color.red);
	}

}
