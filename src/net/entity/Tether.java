package net.entity;

import net.Entanglement;
import net.grid.Block;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class Tether extends Entity {
	Block block1;
	Block block2;
	private int alpha;
	private double count;
	private float bgwidth;
	private float width;
	private boolean expanding;

	public Tether(int x, int y, Block b1, Block b2) {
		super(x, y);
		block1 = b1;
		block2 = b2;
		renderDepth = 0.4f;
		count = 0;
		alpha = 60;
		width = 0;
		expanding = true;
	}

	public Block getOtherBlock(Block b) {
		if (b == block1)
			return block2;
		else
			return block1;
	}

	@Override
	public void destroy() {
		block1.tether = null;
		block2.tether = null;
		super.destroy();
	}

	@Override
	public void render() {
		super.render();
		float delta = Entanglement.getDeltaSeconds();
		alpha = (int) (150 + 30 * Math.sin(count));
		if(expanding) {
			width += 16 * delta;
			bgwidth += 20*delta;
			if(width > 4) {
				width = 4;
				expanding = false;
			}
		} else {
			width = 4;
			bgwidth = (float) (8 + 3 * Math.sin(count));
		}
		count += 6.283 * delta;
		if (count >= 6.283)
			count = 0;
		float x1 = block1.spriteX + 16;
		float y1 = block1.spriteY + 16;
		float x2 = block2.spriteX + 16;
		float y2 = block2.spriteY + 16;
		// Background white line
		Renderer.drawLine(x1, y1, x2, y2, bgwidth, renderDepth+0.01f, new Color(255,
				255, 255, alpha), new Color(255, 255, 255, alpha));
		// Colored line
		Renderer.drawLine(x1, y1, x2, y2, width, renderDepth, new Color(152, 182,
				252, alpha), new Color(255, 165, 104, alpha));
	}

	@Override
	public void beginStep() {

	}

	@Override
	public void endStep() {

	}

}
