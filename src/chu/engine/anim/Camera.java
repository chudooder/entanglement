package chu.engine.anim;

import static org.lwjgl.opengl.GL11.glTranslatef;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Stage;

public class Camera {
	
	protected Entity center;
	protected int offsetX;
	protected int offsetY;
	protected Stage stage;
	
	public Camera(Entity e, int oX, int oY) {
		set(e, oX, oY);
	}
	
	public void set(Entity e, int oX, int oY) {
		center = e;
		offsetX = oX;
		offsetY = oY;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void lookThrough() {
		glTranslatef(-((int)getScreenX()), -((int)getScreenY()), 0);
	}
	
	public void lookBack() {
		glTranslatef((int)getScreenX(), (int)getScreenY(), 0);
	}
	
	
	/**
	 * @return the absolute X position of this camera
	 */
	public float getX() {
		if(center != null)
			return center.x + offsetX;
		else
			return Game.getWindowWidth()/2;
	}
	
	/**
	 * @return the absolute Y position of this camera
	 */
	public float getY() {
		if(center != null)
			return center.y + offsetY;
		else
			return Game.getWindowHeight()/2;
	}
	
	/**
	 * @return the absolute X location of the left edge of the window view
	 */
	public int getScreenX() {
		if(center != null)
			return (int) (center.x + offsetX - Game.getWindowWidth()/2);
		else
			return 0;
	}
	
	/**
	 * @return the absolute Y location of the top edge of the window view
	 */
	public int getScreenY() {
		if(center != null)
			return (int) (center.y + offsetY - Game.getWindowHeight()/2);
		else
			return 0;
	}

}
