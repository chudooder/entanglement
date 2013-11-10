package net.grid;

import java.io.IOException;

import net.Entanglement;
import net.Level;
import net.entity.GriddedEntity;
import net.entity.KickHitbox;
import net.entity.Tether;
import net.stage.EntanglementStage;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Collidable;
import chu.engine.Direction;
import chu.engine.Entity;
import chu.engine.RectangleHitbox;
import chu.engine.anim.Sprite;

public class Block extends GriddedEntity implements Collidable {
	
	private static Texture block_blue;
	private static Texture block_red;
	private static Texture block_yellow;
	private static Texture block;
	private static Texture blue_glow;
	private static Texture orange_glow;
	public Tether tether;
	private int energized;
	private int color;
	private Sprite glow;
	public float spriteY;
	public float spriteX;
	public float spriteVY;
	public float spriteVX;
	
	static {
		try {
			block_blue = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/block_blue.png"));
			block_red = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/block_red.png"));
			block_yellow = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/block_yellow.png"));
			block = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/block.png"));
			blue_glow = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/blue_glow.png"));
			orange_glow = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/orange_glow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public Block(int x, int y, int colorArg) {
		super(x, y);
		glow = new Sprite();
		glow.addAnimation("BLUE", blue_glow, 32, 32, 4, 75);
		glow.addAnimation("ORANGE", orange_glow, 32, 32, 4, 75);
		if(colorArg == 0) {
			sprite.addAnimation("BLUE", block_blue, 32, 32, 2, 0);
		} else if(colorArg == 1) {
			sprite.addAnimation("RED", block_red, 32, 32, 2, 0);
		} else if(colorArg == 2) {
			sprite.addAnimation("YELLOW", block_yellow, 32, 32, 2, 0);
		} else {
			sprite.addAnimation("NO_COLOR", block, 32, 32, 2, 0);
		}
		hitbox = new RectangleHitbox(this, 0, 0, 32, 32);
		solid = true;
		tether = null;
		color = colorArg;
		renderDepth = 0.7f;
		spriteY = y*32;
		spriteX = x*32;
		spriteVY = 0;
		spriteVX = 0;
		width = 32;
		height = 32;
	}
	
	@Override
	public void render() {
		sprite.render(spriteX, spriteY, renderDepth);
		if(energized != 0) glow.render(spriteX, spriteY, 0.49f);
	}

	@Override
	public void beginStep() {
		super.beginStep();
		if(testMove(Direction.SOUTH) && (y - spriteY) < 16) move(Direction.SOUTH);
	}
	
	@Override
	public void onStep() {
		super.onStep();
		glow.update();
		float delta = Entanglement.getDeltaSeconds();
		//update the spriteX
		spriteVX = (x - spriteX)*20;
		float dx = Math.min(Math.abs(spriteVX*delta), Math.abs(x - spriteX));
		if(spriteVX > 0)
			spriteX += dx;
		else
			spriteX -= dx;
		if(Math.abs(x - spriteX) < 1) {
			spriteX = x;
			spriteVX = 0;
		}
		//update the spriteY
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
		if(Math.abs(y-spriteY) < 1) {
			spriteY = y;
			spriteVY = 0;
		}
		spriteY += spriteVY*delta;
	}

	@Override
	public void endStep() {

		
	}

	@Override
	public void doCollisionWith(Entity entity) {
		if(entity instanceof KickHitbox) {
			Direction d = ((KickHitbox)entity).direction;
			entity.destroy();
			move(d);
		}
		
	}

	public boolean isTethered() {
		return !(tether == null);
	}
	
	public Tether getTether() {
		return tether;
	}

	public void setTether(Tether object) {
		if(object == null && tether != null) {
			tether.destroy();
		} else {
			tether = object;
		}
	}
	
	public void setEnergized(int b) {
		energized = b;
		if(energized != 0) {
			sprite.setFrame(1);
			if(energized == 1) glow.setAnimation("BLUE");
			else if(energized == 2) glow.setAnimation("ORANGE");
		}
		else sprite.setFrame(0);
		

	}

	public int getColor() {
		return color;
	}
	
	@Override
	public boolean move(Direction d) {
		Level level = ((EntanglementStage)stage).getLevel();
		if(tether != null) {
			if(testMove(d) && tether.getOtherBlock(this).testMove(d)) {
				level.move(xcoord, ycoord, d);
				level.move(tether.getOtherBlock(this).xcoord,
						tether.getOtherBlock(this).ycoord, d);
				return true;
			}
			return false;
		} else {
			return level.move(xcoord, ycoord, d);
		}
	}

}
