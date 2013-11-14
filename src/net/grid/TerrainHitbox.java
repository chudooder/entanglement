package net.grid;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.RectangleHitbox;
import chu.engine.anim.Renderer;

/**
 * Hitboxes for the terrain that can be of any rectangular size. These are
 * generated at the start of the level, and serve to reduce the amount of
 * collision checks between the player and solid surfaces.
 * 
 * The generation algorithm is not necessarily the best possible solution, but
 * it should be pretty good.
 * 
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
		renderDepth = 0.55f;
	}

	@Override
	public void beginStep() {
		
	}

	@Override
	public void endStep() {
		
	}

	public void render() {
		Color shadow = new Color(0, 0, 0, 100);
		Renderer.drawRectangle(x, y + height, x + width, y + height + 2,
				renderDepth, shadow, shadow, shadow, shadow);
	}

}
