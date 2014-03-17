package net.entity;

import java.io.IOException;

import net.Entanglement;
import net.grid.Block;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Collidable;
import chu.engine.Direction;
import chu.engine.Entity;
import chu.engine.RectangleHitbox;
import chu.engine.anim.Transform;

/**
 * literally call of duty
 * @author Shawn
 *
 */
public class Shot extends Entity implements Collidable {
	
	private static Texture blue_shot;
	private static Texture orange_shot;
	private Direction direction;
	private float timer;
	public Player player;
	public int type;
	
	static {
		try {
			blue_shot = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/blue_shot.png"));
			orange_shot = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/orange_shot.png"));
		} catch (IOException e) {
			
		}
	}
	
	public Shot(float x, float y, Direction d, int type, Player p) {
		super(x, y);
		if(type == 1) {
			sprite.addAnimation("DEFAULT", blue_shot, 32, 32, 2, 50);
		} else {
			sprite.addAnimation("DEFAULT", orange_shot, 32, 32, 2, 50);
		}
		hitbox = new RectangleHitbox(this, 4, 12, 24, 8);
		direction = d;
		player = p;
		timer = 0;
		this.type = type;
	}

	@Override
	public void doCollisionWith(Entity entity) {
		if(entity instanceof Block) {
			Block b = (Block) entity;
			if(b.spriteVY != 0) return;
			b.sprite.setFrame(1);
			player.setTether(type, b);
			if(type == 1)
				stage.addEntity(new Ping(b.x+16,b.y+16,1, new Color(217, 222, 255)));
			else
				stage.addEntity(new Ping(b.x+16,b.y+16,1, new Color(255, 238, 217)));
			this.destroy();
		}
	}

	@Override
	public void beginStep() {
		x += direction.getUnitX() * 0.5f * Entanglement.getDeltaMillis();
		timer += Entanglement.getDeltaMillis();
	}

	@Override
	public void endStep() {
		if(timer > 2000.0f)
			this.destroy();
	}
	
	@Override
	public void render() {
		Transform t = new Transform();
		if(direction == Direction.WEST) t.flipHorizontal();
		sprite.renderTransformed(x, y, renderDepth, t);
	}

}
