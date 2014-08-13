package net;

import static org.lwjgl.opengl.GL11.glTranslatef;
import net.entity.Player;
import net.stage.EntanglementStage;
import chu.engine.Direction;
import chu.engine.Game;
import chu.engine.anim.Camera;

/**
 * A Camera that floats somewhat in front of the player's view,
 * a la Cave Story camera system. It is locked to only show up
 * to the level boundaries.
 * @author Shawn
 *
 */
public class FloatyCamera extends Camera {
	
	private Player player;
	private static final float MAX_FLOAT_DIST = 100.0f;		// The distance in front of the player to float the camera
	private static final float MAX_DRIFT_SPEED = 128.0f;	// Maximum speed to move the camera
	private static final float DRIFT_ACCEL = 200.0f;
	private static final float DRIFT_DECEL = 100.0f;
	
	private float targetX, targetY;
	private float currentX, currentY;
	private float vx, vy;
	

	public FloatyCamera(Player player, int oX, int oY) {
		super(player, oX, oY);
		this.player = player;
		currentX = Game.getWindowWidth()/2-offsetX;
		currentY = Game.getWindowHeight()/2-offsetY;
		vx = 0;
		vy = 0;
	}
	
	@Override
	public void lookThrough() {		// since this is called every frame we can use it to update stuff
		Direction facing = player.getFacingDirection();
		float delta = Game.getDeltaSeconds();
		// calculate target y, clamp to not show past level boundaries
		float maxY = ((EntanglementStage)stage).level.getHeight()*32-Game.getWindowHeight()/2;
		targetY = clamp(Game.getWindowHeight()/2-offsetY, player.y, maxY-offsetY);
		// calculate target x, clamp to not show past level boundaries
		float maxX = ((EntanglementStage)stage).level.getWidth()*32-Game.getWindowWidth()/2;
		targetX = clamp(Game.getWindowWidth()/2-offsetX, player.x + MAX_FLOAT_DIST * facing.getUnitX(), maxX-offsetX);
		// accelerate the camera if it is far from the target location
		if(Math.abs(currentX - targetX) > decelThreshold() || (targetX - currentX)*vx < 0) {
			vx += DRIFT_ACCEL * delta * Math.signum(targetX - currentX);
		} else if (Math.abs(currentX - targetX) < 2) {
			vx = 0;
		} else {
			vx = (targetX - currentX) * MAX_DRIFT_SPEED / decelThreshold();
		}
		vx = clamp(-MAX_DRIFT_SPEED, vx, MAX_DRIFT_SPEED);
		currentX += vx * delta;
		currentY = targetY;
		
		glTranslatef(-((int)getScreenX()), -((int)getScreenY()), 0);
	}
	
	/**
	 * Calculate the threshold at which a drifting camera should start slowing down
	 * @return
	 */
	private float decelThreshold() {
		return (float) (Math.pow(vx, 2)/2/MAX_DRIFT_SPEED);
	}
	
	private float clamp(float min, float val, float max) {
		if(val > max) return max;
		if(val < min) return min;
		return val;
	}
	
	public int getScreenX() {
		if(center != null)
			return (int) (currentX + offsetX - Game.getWindowWidth()/2);
		else
			return 0;
	}
	
	public int getScreenY() {
		if(center != null)
			return (int) (currentY + offsetY - Game.getWindowHeight()/2);
		else
			return 0;
	}

}
