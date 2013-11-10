package net.entity;

import java.util.HashMap;

import org.newdawn.slick.Color;

import net.Entanglement;
import chu.engine.Entity;

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
		HashMap<Integer, Boolean> keys = Entanglement.getKeys();
		for(int key : keys.keySet()) {
			if(keys.get(key)) {
				active = true;
			}
		}
	}

	@Override
	public void endStep() {

	}

	@Override
	public void render() {
		if(active) {
			String text = String.format("%2d:%02d.%03d", (int) (time / 60000),
					(int) (time / 1000 % 60), (int) (time % 1000));
			int width = Entanglement.sourceSans16.getWidth(text);
			Entanglement.sourceSans16.drawString(600 - width, 0, text, Color.black);
		} else {
			String text = "Press a key to start the timer.";
			int width = Entanglement.sourceSans16.getWidth(text);
			Entanglement.sourceSans16.drawString(600 - width, 0, text, Color.black);
		}
	}

	public double getTime() {
		return time;
	}
}
