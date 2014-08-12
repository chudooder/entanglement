package net.entity;

import java.util.List;

import net.Entanglement;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.KeyboardEvent;

/**
 * self-esteem wrecker
 * @author Shawn
 *
 */
public class RunTimer extends Entity {

	private double time;
	private boolean active;

	public RunTimer() {
		super(0, 0);
		time = 0;
		renderDepth = 0.0f;
		active = false;
	}

	@Override
	public void beginStep() {
		if(active) time += Entanglement.getDeltaMillis();
		//Timing begins when you press any key
		List<KeyboardEvent> keys = Entanglement.getKeys();
		for(KeyboardEvent ke : keys) {
			if(ke.state) {
				active = true;
			}
		}
	}

	@Override
	public void endStep() {

	}

	@Override
	@SuppressWarnings("deprecation")
	public void render() {
		float offsetX = stage.getCamera().getScreenX();
		float offsetY = stage.getCamera().getScreenY();
		if(active) {
			String text = String.format("%2d:%02d.%03d", (int) (time / 60000),
					(int) (time / 1000 % 60), (int) (time % 1000));
			int width = Entanglement.sourceSans16.getWidth(text);
			Entanglement.sourceSans16.drawString(offsetX + 600 - width, offsetY, text, Color.black);
		} else {
			String text = "Press a key to start the timer.";
			int width = Entanglement.sourceSans16.getWidth(text);
			Entanglement.sourceSans16.drawString(offsetX + 600 - width, offsetY, text, Color.black);
		}
	}

	public double getTime() {
		return time;
	}
}
