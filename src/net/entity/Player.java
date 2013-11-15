package net.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

import net.Entanglement;
import net.Settings;
import net.grid.Block;
import net.grid.Exit;
import net.grid.Hat;
import net.stage.EntanglementStage;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Collidable;
import chu.engine.Direction;
import chu.engine.Entity;
import chu.engine.Hitbox;
import chu.engine.RectangleHitbox;
import chu.engine.anim.Animation;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;

public class Player extends Entity implements Collidable {

	private static Texture cookie;
	private static Texture cookie_jump;
	private static Texture cookie_run;
	private static Texture cookie_land;
	private static Texture hat;
	private static Texture cookie_happy;
	private boolean isGrounded;
	private boolean happy;
	public float vy;
	private float vx;
	private Direction facing;
	private float jumpBoostTimer;
	private Block blueTether;
	private Block orangeTether;

	static {
		try {
			cookie = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/cookie.png"));
			cookie_jump = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/cookie_jump.png"));
			cookie_run = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/cookie_run.png"));
			cookie_land = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/cookie_land.png"));
			hat = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/hat.png"));
			cookie_happy = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/cookie_happy.png"));
			System.out.println("Loaded player sprites");
		} catch (IOException e) {

		}
	}

	public Player(float x, float y) {
		super(x, y);
		sprite.addAnimation("JUMP", cookie_jump, 32, 32, 5, 0);
		sprite.addAnimation("RUN", cookie_run, 32, 32, 8, 75);
		Animation land = new Animation(cookie_land, 32, 32, 2, 75) {
			public void done() {
				if(vx == 0) sprite.setAnimation("IDLE");
				else sprite.setAnimation("RUN");
			}
		};
		sprite.addAnimation("LAND", land);
		sprite.addAnimation("IDLE", cookie);
		width = 24;
		height = 24;
		hitbox = new RectangleHitbox(this, 4, 8, (int)width, (int)height);
		setGrounded(true);
		vy = 0;
		vx = 0;
		facing = Direction.EAST;
		blueTether = null;
		orangeTether = null;
		renderDepth = 0.6f;
		jumpBoostTimer = 0;
		if(Settings.HATNUM == 18) {
			sprite.addAnimation("HAPPY", cookie_happy);
			sprite.setAnimation("HAPPY");
			happy = true;
		} else {
			happy = false;
		}
	}

	@Override
	public void beginStep() {
		HashMap<Integer, Boolean> inputs = Entanglement.getKeys();
		float delta = Entanglement.getDeltaSeconds();
		/* Movement */
		if (Keyboard.isKeyDown(Settings.getKey(Settings.K_LEFT))) {
			vx -= 800 * delta;
			facing = Direction.WEST;
			if(isGrounded() && !happy) sprite.setAnimation("RUN");
		} else if (Keyboard.isKeyDown(Settings.getKey(Settings.K_RIGHT))) {
			vx += 800 * delta;
			facing = Direction.EAST;
			if(isGrounded() && !happy) sprite.setAnimation("RUN");
		}
		if(vx < -128) vx = -128;
		if(vx > 128) vx = 128;
		if (!(Keyboard.isKeyDown(Settings.getKey(Settings.K_LEFT)) || Keyboard
				.isKeyDown(Settings.getKey(Settings.K_RIGHT)))) {
			if (vx > 0) {
				vx = Math.max(0, vx - 800 * delta);
			}
			if (vx < 0) {
				vx = Math.min(0, vx + 800 * delta);
			}
		}
		x += vx * delta;
		if(x < 0) x = 0;
		if(x > 640-32) x = 640-32;
		for (Integer key : inputs.keySet()) {
			if (inputs.get(key) && key == Settings.getKey(Settings.K_JUMP) && isGrounded()) {
				setGrounded(false);
				vy = -250f;
				if(!happy) sprite.setAnimation("JUMP");
			}
		}
		float gravity = 800;
		if(Keyboard.isKeyDown(Settings.getKey(Settings.K_JUMP)) && jumpBoostTimer < .35f) {
			gravity = 150;
			jumpBoostTimer += Entanglement.getDeltaSeconds();
		}
		
		if(!isGrounded()) {
			vy += gravity * delta;
			y += vy * delta;
		} else {
			if(tryMove(0, 1) == true) {
				setGrounded(false);
				if(!happy) sprite.setAnimation("JUMP");
			} else {
				vy = 0;
				jumpBoostTimer = 0;
				if(vx == 0 && !happy) sprite.setAnimation("IDLE");
			}
		}

		/* Kicking */
		
		for(int key : inputs.keySet()) {
			if(inputs.get(key)) {
				if (key == Settings.getKey(Settings.K_KICK)) {
					stage.addEntity(new KickHitbox(x+8+12*facing.getUnitX(), y/32*32, facing));
					vx += 150*facing.getUnitX();
				}
				
				if (key == Settings.getKey(Settings.K_FIRE1)) {
					stage.addEntity(new Shot(x, y, facing, 1, this));
				}
				
				if (key == Settings.getKey(Settings.K_FIRE2)){ 
					stage.addEntity(new Shot(x, y, facing, 2, this));
				}
				
				if (key == Settings.getKey(Settings.K_CLEAR)) {
					if(blueTether != null) {
						blueTether.setTether(null);
						blueTether.setEnergized(0);
					}
					if(orangeTether != null) {
						orangeTether.setTether(null);
						orangeTether.setEnergized(0);
					}
					blueTether = null;
					orangeTether = null;
				}
				
				if (key == Settings.getKey(Settings.K_RESET)) {
					((EntanglementStage)stage).resetLevel();
				}
			}
		}
		
		if(y > 800) {
			((EntanglementStage)stage).resetLevel();
		}
	}

	@Override
	public void endStep() {
		
	}

	@Override
	public void render() {
		Transform t = new Transform();
		if(facing == Direction.WEST) t.flipHorizontal();
		//for jumping animation
		if(!isGrounded() && !happy) {
			if(vy < -50) sprite.setFrame(0);
			if(vy >= -50) sprite.setFrame(1);
			if(vy >= 0) sprite.setFrame(2);
			if(vy >= 50) sprite.setFrame(3);
		}
		sprite.renderTransformed(x, y, renderDepth, t);
		for(int i=0; i<Settings.HATNUM; i++) {
			Renderer.render(hat, 0, 0, 1, 1, (int)x, (int)y-14-14*i, (int)x+32, (int)y+18-14*i, renderDepth-0.01f);
		}
	}

	@Override
	public void doCollisionWith(Entity entity) {
		if (entity.solid) {
			// Push out of the terrain.
			// Determine the shortest path out of the block.
			float left = Math.abs(entity.x - (x + 4 + width));
			float right = Math.abs(entity.x + entity.width - (x + 4));
			float up = Math.abs(entity.y - (y + 32));
			float down = Math.abs(entity.y + entity.height - (y+8));
			float best = Math.min(Math.min(left, right), Math.min(up, down));
			if (best == right) {
				x = entity.x + entity.width - 4;
				vx = 0;
			} else if (best == left) {
				x = entity.x - (width + 4);
				vx = 0;
			} else if (best == up && vy >= 0) {
				y = entity.y - height - 8;
				setGrounded(true);
				vy = 0;
			} else if (best == down) {
				y = entity.y + entity.height - 8;
				if (vy < 0)
					vy = 0;
			}
		}
		
		if(entity instanceof Exit) {
			if(((Exit)entity).open) {
				System.out.println("EXIT");
				((EntanglementStage)stage).completeLevel();
			}
		}
		
		if(entity instanceof Hat) {
			System.out.println("HAT ACQUIRED");
			entity.destroy();
			Settings.gotHat(((EntanglementStage)stage).getLevel().getName());
		}
	}
	
	public void setTether(int type, Block b) {		
		if (type == 0 || type == 1) {
			if(blueTether != null) {
				blueTether.setEnergized(0);
				if(blueTether.isTethered()) 
					blueTether.setTether(null);
			}
			if(orangeTether == b) {
				orangeTether.setTether(null);
				orangeTether = null;
			}
			blueTether = b;
		}
		if (type == 0 || type == 2) {
			if(orangeTether != null) {
				orangeTether.setEnergized(0);
				if(orangeTether.isTethered()) 
					orangeTether.setTether(null);
			}
			if(blueTether == b) {
				blueTether.setTether(null);
				blueTether = null;
			}
			orangeTether = b;
		}
		
		Tether tether = null;
		if (blueTether != null
				&& orangeTether != null
				&& (blueTether.getColor() == orangeTether.getColor()
						|| blueTether.getColor() == 3 || orangeTether
						.getColor() == 3)) {
			tether = new Tether(0, 0, blueTether, orangeTether);
					stage.addEntity(tether);
		}
		if(blueTether != null) {
			blueTether.setTether(tether);
			if(b == blueTether) blueTether.setEnergized(type);
		}
		if(orangeTether != null) {
			orangeTether.setTether(tether);
			if(b == orangeTether) orangeTether.setEnergized(type);
		}
	}
	
	public boolean isGrounded() {
		return isGrounded;
	}

	public void setGrounded(boolean isGrounded) {
		this.isGrounded = isGrounded;
	}

	private boolean tryMove(float dx, float dy) {
		TreeSet<Entity> entities = stage.getAllEntities();
		Entity[] ent = new Entity[entities.size()];
		entities.toArray(ent);
		float oldX = x;
		float oldY = y;
		x += dx;
		y += dy;
		for(Entity e : ent) {
			if(e != this && Hitbox.collisionExists(e, this) == 1 && e.solid) {
				x = oldX;
				y = oldY;
				return false;
			}
		}
		x = oldX;
		y = oldY;
		return true;
	}
}
