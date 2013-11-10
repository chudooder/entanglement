package net.grid;

import java.io.IOException;

import net.entity.Buttonable;
import net.entity.GriddedEntity;
import net.entity.Player;
import net.stage.EntanglementStage;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Collidable;
import chu.engine.Entity;
import chu.engine.RectangleHitbox;

public class Button extends Entity implements Collidable {

	private static Texture no_color;
	private static Texture blue;
	private static Texture red;
	private static Texture yellow;
	private boolean isPushed;
	private boolean inContact;
	private int color;

	static {
		try {
			no_color = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/button.png"));
			blue = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/button_blue.png"));
			red = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/button_red.png"));
			yellow = TextureLoader
					.getTexture("PNG", ResourceLoader
							.getResourceAsStream("res/button_yellow.png"));
			System.out.println("Loaded button sprites");
		} catch (IOException e) {

		}
	}

	public Button(float x, float y, int color) {
		super(x, y);
		this.color = color;
		if (color == 3)
			sprite.addAnimation("NO_COLOR", no_color, 32, 32, 2, 0);
		if (color == 0)
			sprite.addAnimation("BLUE", blue, 32, 32, 2, 0);
		if (color == 1)
			sprite.addAnimation("RED", red, 32, 32, 2, 0);
		if (color == 2)
			sprite.addAnimation("YELLOW", yellow, 32, 32, 2, 0);
		hitbox = new RectangleHitbox(this, 7, 21, 18, 11);
		isPushed = false;
		renderDepth = 0.69f;
	}

	@Override
	public void beginStep() {
		inContact = false;
	}

	@Override
	public void endStep() {
		if (isPushed && !inContact) {
			sprite.setFrame(0);
			isPushed = false;
			int[][] levelWires = ((EntanglementStage) stage).level.getWires();
			int[][] wires = new int[levelWires.length][levelWires[0].length];
			for (int i = 0; i < wires.length; i++) {
				for (int j = 0; j < wires[0].length; j++) {
					wires[i][j] = levelWires[i][j];
				}
			}
			int init = wires[(int)(y/32)][(int)(x/32)];
			sendSignal((int) x / 32, (int) y / 32, wires, false, init);
		}
	}

	@Override
	public void doCollisionWith(Entity entity) {
		if (!isPushed) {
			if (entity instanceof Player || entity instanceof Block
					&& (((Block) entity).getColor() == color || color == 3)) {
				isPushed = true;
				inContact = true;
				sprite.setFrame(1);
				int[][] levelWires = ((EntanglementStage) stage).level
						.getWires();
				int[][] wires = new int[levelWires.length][levelWires[0].length];
				for (int i = 0; i < wires.length; i++) {
					for (int j = 0; j < wires[0].length; j++) {
						wires[i][j] = levelWires[i][j];
					}
				}
				int init = wires[(int)(y/32)][(int)(x/32)];
				sendSignal((int) x / 32, (int) y / 32, wires, true, init);
			}
		} else {
			if (entity instanceof Player || entity instanceof Block
					&& (((Block) entity).getColor() == color || color == 3)) {
				inContact = true;
			}
		}
	}

	private void sendSignal(int x, int y, int[][] wires, boolean press, int init) {
		if (x < 0 || x >= wires[0].length || y < 0 || y >= wires.length)
			return;
		if (wires[y][x] == 0)
			return;
		if (wires[y][x] == init) {
			wires[y][x] = 0;
			GriddedEntity ge = ((EntanglementStage) stage).level
					.getEntity(x, y);
			if (ge != null && ge instanceof Buttonable) {
				if (press) {
					((Buttonable) ge).doPressEvent();
				} else {
					((Buttonable) ge).doReleaseEvent();
				}
			}
			sendSignal(x + 1, y, wires, press, init);
			sendSignal(x - 1, y, wires, press, init);
			sendSignal(x, y + 1, wires, press, init);
			sendSignal(x, y - 1, wires, press, init);
		}
	}

}
