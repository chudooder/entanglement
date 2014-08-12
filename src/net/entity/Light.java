package net.entity;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class Light extends Entity {
	
	public static List<Light> lights = new ArrayList<Light>();

	public Light(float x, float y) {
		super(x, y);
		lights.add(this);
	}
	
	public void beginStep() {
		x = Mouse.getX();
		y = 480 - Mouse.getY();
	}
	
	public void render() {
		Renderer.drawRectangle(x-2, y-2, x+2, y+2, 0.0f, Color.white);
	}
	
	public void destroy() {
		lights.remove(this);
		super.destroy();
	}

}
