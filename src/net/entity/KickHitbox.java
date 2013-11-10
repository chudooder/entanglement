package net.entity;

import chu.engine.Direction;
import chu.engine.Entity;
import chu.engine.RectangleHitbox;

public class KickHitbox extends Entity {
	
	public Direction direction;
	public KickHitbox(float x, float y, Direction d) {
		super(x, y);
		hitbox = new RectangleHitbox(this, 1, 6, 16, 20);
		direction = d;
	}

	@Override
	public void beginStep() {
		//exist
	}

	@Override
	public void endStep() {
		this.destroy();
	}
}
