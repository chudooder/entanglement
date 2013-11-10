package net.grid;

import java.io.IOException;

import net.entity.Buttonable;
import net.entity.GriddedEntity;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.RectangleHitbox;
import chu.engine.anim.Animation;

public class Exit extends GriddedEntity implements Buttonable {

	public static Texture open_tex;
	public static Texture closed_tex;
	public static Texture slide_open;
	public static Texture slide_closed;
	public boolean open;
	
	static {
		try {
			open_tex = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/exit.png"));
			closed_tex = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/exit_closed.png"));
			slide_open = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/exit_slide_open.png"));
			slide_closed = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/exit_slide_closed.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Exit(int x, int y, boolean open) {
		super(x, y);
		Animation slideOpen = new Animation(slide_open, 32, 32, 4, 75) {
			public void done() {
				sprite.setAnimation("OPEN");
			}
		};
		Animation slideClosed = new Animation(slide_closed, 32, 32, 4, 75) {
			public void done() {
				sprite.setAnimation("CLOSED");
			}
		};
		sprite.addAnimation("SLIDE_OPEN", slideOpen);
		sprite.addAnimation("SLIDE_CLOSED", slideClosed);
		sprite.addAnimation("OPEN", open_tex, 32, 32, 8, 100);
		sprite.addAnimation("CLOSED", closed_tex);
		if(open) sprite.setAnimation("OPEN");
		hitbox = new RectangleHitbox(this, 6, 4, 10, 24);
		this.open = open;
	}

	@Override
	public void beginStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub
		
	}
	
	private void changeState() {
		open = !open;
		if(open) {
			sprite.setAnimation("SLIDE_OPEN");
			sprite.setFrame(0);
		} else {
			sprite.setAnimation("SLIDE_CLOSED");
			sprite.setFrame(0);
		}
	}

	@Override
	public void doPressEvent() {
		changeState();
	}

	@Override
	public void doReleaseEvent() {
		changeState();
	}

}
