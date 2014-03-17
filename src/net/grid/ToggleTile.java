package net.grid;

import java.io.IOException;

import net.entity.Buttonable;
import net.entity.GriddedEntity;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.RectangleHitbox;
import chu.engine.anim.Animation;

/**
 * Toggles between solid and not solid
 * @author Shawn
 *
 */
public class ToggleTile extends GriddedEntity implements Buttonable {
	
	private static Texture toggle_tile;
	
	static {
		try {
			toggle_tile = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/toggle_tile.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ToggleTile(int x, int y, int arg) {
		super(x, y);
		solid = (arg == 0);
		width = 32;
		height = 32;
		Animation anim = new Animation(toggle_tile, 32, 32, 4, 0) {
			public void done() {
				if(speed > 0) {
					sprite.setFrame(3);
					sprite.setSpeed(0);
				} else if (speed < 0) {
					sprite.setFrame(0);
					sprite.setSpeed(0);
				}
			}
		};
		sprite.addAnimation("DEFAULT", anim);
		if(!solid)
			sprite.setFrame(3);
		hitbox = new RectangleHitbox(this, 0, 0, 32, 32);
		renderDepth = 0.7f;
	}
	
	public void toggle() {
		solid = !solid;
		if(solid) {
			sprite.setSpeed(-75);
		} else {
			sprite.setSpeed(75);
		}
	}

	@Override
	public void doPressEvent() {
		System.out.println("Press");
		toggle();
	}

	@Override
	public void doReleaseEvent() {
		System.out.println("Release");
		toggle();
	}

	@Override
	public void beginStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub
		
	}

}
