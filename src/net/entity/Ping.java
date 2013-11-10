package net.entity;

import java.io.IOException;

import net.Entanglement;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Particle;
import chu.engine.anim.Transform;

public class Ping extends Particle {
	
	float scale;
	float ds;
	private static Texture texture;
	private Color color;
	
	static {
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/ping.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Ping(float x, float y, int lifetime, Color c) {
		super(x, y, lifetime);
		scale = 0.01f;
		renderDepth = 0.1f;
		ds = 1.5f;
		color = c;
		sprite.addAnimation("DEFAULT", texture);
	}
	
	@Override
	public void beginStep() {
		super.beginStep();
		scale += ds * Entanglement.getDeltaSeconds();
		ds -= 1.5f * Entanglement.getDeltaSeconds();
		color.a = ds;
	}
	
	@Override
	public void render() {
		Transform t = new Transform();
		t.setScale(scale, scale);
		t.setColor(color);
		float offset = scale*64;
		sprite.renderTransformed(x-offset, y-offset, renderDepth, t);
	}

}
