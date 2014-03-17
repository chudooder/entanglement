package net.grid;

import chu.engine.RectangleHitbox;
import net.entity.GriddedEntity;

/**
 * most important class in this project, gets additional
 * documentation
 * @author Shawn
 *
 */
public class Hat extends GriddedEntity {

	public Hat(int xx, int yy, int arg) {
		super(xx, yy);
		renderDepth = 0.7f;
		hitbox = new RectangleHitbox(this, 4, 4, 26, 26);
	}

}
