package net.grid;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;

/**
 * it's good for your bones
 * @author Shawn
 *
 */
public class Milk extends Entity {
	
	public static Texture surface;
	boolean isSurface;
	
	static {
		try {
			surface = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/milk_surface.png"));
			System.out.println("Loaded milk sprites");
		} catch (IOException e) {

		}
	}
	public Milk(int xx, int yy, int arg) {
		super(xx, yy);
		isSurface = (arg==0);
		sprite.addAnimation("SURFACE", surface, 32, 32, 16, .075f);
		sprite.setFrame((int) (Math.random()*16));
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
