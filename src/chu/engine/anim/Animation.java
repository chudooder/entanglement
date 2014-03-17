package chu.engine.anim;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;

public class Animation {
	private Texture texture;
	private int width;
	private int height;
	private int length;
	private int currentFrame;
	private float counter;
	protected float speed;			//Time for each frame in seconds
	
	public Animation(Texture t) {
		texture = t;
		width = t.getImageWidth();
		height = t.getImageHeight();
		length = 1;
		speed = 0;
	}
	
	public Animation(Texture t, int width, int height, int frames, float speed) {
		this(t);
		this.width = width;
		this.height = height;
		this.length = frames;
		this.speed = speed;
	}
	
	public int getLength() { return length; }
	public int getFrame() { return currentFrame; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getImageWidth() { return texture.getImageWidth(); }
	public Texture getTexture() { return texture; }
	
	
	public void update() {
		if(speed != 0) counter += Game.getDeltaSeconds();
		if(speed != 0 && counter > Math.abs(speed)) {
			increment();
			counter = 0;
		}
	}
	
	public void increment() {
		if(speed > 0) {
			currentFrame += 1;
			if(currentFrame >= length) done();
		} else {
			currentFrame -= 1;
			if(currentFrame < 0) done();
		}
	}
	
	public void done() {
		if(speed > 0)
			currentFrame = 0;
		else
			currentFrame = length-1;
	}

	public void setFrame(int i) {
		this.currentFrame = i;
	}

	public void setSpeed(float f) {
		speed = f;
	}

	public float getSpeed() {
		return speed;
	}
	

}