package net.entity;

import net.grid.LiftPlatform;
import chu.engine.Collidable;
import chu.engine.Entity;
import chu.engine.RectangleHitbox;

public class PlatformZone extends Entity implements Collidable {
	
	public LiftPlatform parent;
	public Player player;

	public PlatformZone(float x, float y, LiftPlatform platform) {
		super(x, y);
		parent = platform;
		hitbox = new RectangleHitbox(this, 0, 0, 32, 1);
		player = null;
		solid = false;
	}

	@Override
	public void beginStep() {
		
	}

	@Override
	public void endStep() {
		y = parent.spriteY - 1;
		if(player != null) {
			player.y = parent.spriteY - 32;
			player.setGrounded(true);
		}
		player = null;
	}

	@Override
	public void doCollisionWith(Entity entity) {
		if(entity instanceof Player) {
			Player p = (Player)entity;
			if(p.vy >= 0) player = p;
		}
	}
	
}
