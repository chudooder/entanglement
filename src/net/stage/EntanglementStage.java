package net.stage;

import java.util.HashMap;

import net.Entanglement;
import net.Level;
import net.entity.RunTimer;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Collidable;
import chu.engine.Entity;
import chu.engine.Hitbox;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class EntanglementStage extends Stage {

	public Level level;
	private boolean reset;
	private boolean timing;
	private RunTimer timer;

	public EntanglementStage() {
		super();
		timing = false;
	}

	public EntanglementStage(boolean timing) {
		super();
		this.timing = timing;
		if (timing) {
			timer = new RunTimer();
			addEntity(timer);
		}
	}

	public Level getLevel() {
		return level;
	}

	public void resetLevel() {
		reset = true;
	}

	public void completeLevel() {
		if (timing)
			Entanglement.selectStage.completeLevel(timer.getTime());
		else
			Entanglement.selectStage.completeLevel();
		Entanglement.setCurrentStage(Entanglement.selectStage);
	}

	public void beginStep() {
		HashMap<Integer, Boolean> keys = Entanglement.getKeys();
		for (int key : keys.keySet()) {
			if (keys.get(key)) {
				if (key == Keyboard.KEY_ESCAPE) {
					Entanglement.setCurrentStage(Entanglement.selectStage);
				}
			}
		}
		for (Entity e : entities) {
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
	}

	public void onStep() {
		for (Entity e : entities) {
			e.onStep();
		}
		resolveCollisions();
		processAddStack();
		processRemoveStack();
	}

	public void endStep() {
		for (Entity e : entities) {
			e.endStep();
		}
		processAddStack();
		processRemoveStack();
		if (reset) {
			Entanglement.setCurrentStage(Entanglement.createStageFromLevel(
					LevelSelectStage.getLevel(level.getName()), timing));
		}
	}

	public void render() {
		Color c0 = new Color(195, 239, 240);
		Color c1 = new Color(224, 248, 248);
		Renderer.drawRectangle(0, 0, 640, 480, 1.0f, c0, c0, c1, c1);
		super.render();
	}

	private void resolveCollisions() {
		Entity[] ent = new Entity[entities.size()];
		entities.toArray(ent);
		for (int a = 0; a < ent.length; a++) {
			if (ent[a] instanceof Collidable) {
				for (int b = 0; b < ent.length; b++) {
					if (a != b && !ent[a].willBeRemoved
							&& !ent[b].willBeRemoved
							&& Hitbox.collisionExists(ent[a], ent[b]) == 1) {
						((Collidable) ent[a]).doCollisionWith(ent[b]);
					}
				}
			}
		}
	}
}
