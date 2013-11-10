package net.grid;

import java.io.IOException;

import net.Entanglement;
import net.entity.GriddedEntity;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.RectangleHitbox;

public class LiftPlatform extends GriddedEntity {
	
	private static Texture lift_platform;
	public float spriteY;
	private float spriteVY;
	
	static {
		try {
			lift_platform = TextureLoader
					.getTexture("PNG", ResourceLoader
							.getResourceAsStream("res/lift_platform.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LiftPlatform(int xx, int yy) {
		super(xx, yy);
		hitbox = new RectangleHitbox(this, 0, 0, 32, 4) {
			public float getX() {
				return x;
			}
			
			public float getY() {
				return spriteY;
			}
		};
		sprite.addAnimation("DEFAULT", lift_platform);
		solid = true;
		width = 32;
		height = 4;
		spriteY = y;
		renderDepth = 0.7f;
	}
	
	@Override
	public void beginStep() {
		super.beginStep();
		//update the spriteY
		float delta = Entanglement.getDeltaSeconds();
		if(spriteY != 0) solid = false;
		if(spriteY < y) {		//fall down
			spriteVY += 600*delta;
		} else {
			if(spriteVY > 0) {
				spriteY = y;
				spriteVY = 0;
			} else {
				spriteVY = (y - spriteY)*10;
			}
		}
		if(Math.abs(y - spriteY) < 1) {
			spriteY = y;
			spriteVY = 0;
			solid = true;
		}
		spriteY += spriteVY*delta;
	}
	
	@Override
	public void render() {
		sprite.render(x, spriteY, renderDepth);
	}

}
