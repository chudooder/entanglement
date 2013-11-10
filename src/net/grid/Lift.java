package net.grid;

import java.io.IOException;

import net.Level;
import net.entity.Buttonable;
import net.entity.GriddedEntity;
import net.stage.EntanglementStage;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Direction;
import chu.engine.RectangleHitbox;
import chu.engine.anim.Renderer;

public class Lift extends GriddedEntity implements Buttonable {

	private static Texture lift_base;
	private static Texture pipe;
	private boolean active;
	private int reach;
	private int platformPos;
	public LiftPlatform platform;

	static {
		try {
			lift_base = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/lift_base.png"));
			pipe = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/pipe.png"));
			System.out.println("Loaded lift sprites");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Lift(int xx, int yy, int arg) {
		super(xx, yy);
		this.reach = arg + 1;
		platformPos = 0;
		width = 32;
		height = 32;
		active = false;
		sprite.addAnimation("DEFAULT", lift_base);
		hitbox = new RectangleHitbox(this, 0, 0, 32, 32);
		solid = true;
		platform = null;
		renderDepth = 0.71f;
	}

	@Override
	public void beginStep() {
		if (active && platformPos < reach) {
			Level level = ((EntanglementStage) stage).getLevel();
			GriddedEntity e = level.getEntity(xcoord, ycoord - platformPos - 1);
			if (e == null || e instanceof Block && ((Block)e).move(Direction.NORTH)) {
				if(platformPos != 0) level.set(xcoord, ycoord - platformPos, null);
				platformPos++;
				level.set(xcoord, ycoord - platformPos, platform);
			}
		} else if (!active && platformPos > 0) {
			Level level = ((EntanglementStage) stage).getLevel();
			if (level.getEntity(xcoord, ycoord - platformPos + 1) == null
					|| platformPos == 1
					|| level.testMove(xcoord, ycoord - platformPos - 1, Direction.SOUTH)) {
				level.set(xcoord, ycoord - platformPos, null);
				platformPos--;
				if(platformPos != 0) {
					level.set(xcoord, ycoord - platformPos, platform);
				} else {
					platform.ycoord += 1;
				}
			}
		}
	}

	@Override
	public void doPressEvent() {
		active = !active;
	}

	@Override
	public void doReleaseEvent() {
//		active = !active;
	}
	
	@Override
	public void render() {
		super.render();
		for(float i=platform.spriteY; i<y+6; i+=4) {
			Renderer.render(pipe, 0, 0, 1, 1, x+12, i, x+20, i+4, renderDepth+.01f);
		}
	}

}
