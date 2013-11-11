package net.entity;

import java.util.ArrayList;

import net.Level;
import net.grid.Block;
import net.stage.EntanglementStage;
import chu.engine.Direction;
import chu.engine.Entity;

public class GriddedEntity extends Entity {

	public int xcoord;
	public int ycoord;

	public GriddedEntity(int xx, int yy) {
		super(xx * 32, yy * 32);
		xcoord = xx;
		ycoord = yy;
	}

	@Override
	public void beginStep() {
		x = xcoord * 32;
		y = ycoord * 32;
	}

	@Override
	public void endStep() {

	}

	public boolean move(Direction d, ArrayList<GriddedEntity> processed) {
		if(processed.contains(this)) {
			return true;
		}
		processed.add(this);
		Level level = ((EntanglementStage) stage).level;
		if (!level.inBounds(xcoord + d.getUnitX(), ycoord + d.getUnitY())) {
			return false;
		}
		GriddedEntity other = level.get(xcoord + d.getUnitX(),
				ycoord + d.getUnitY());
		if (other == null) {
			level.move(xcoord, ycoord, d);
			return true;
		} else if (!(other instanceof Block)) {
			return false;
		} else {
			if (other.move(d, processed)) {
				level.move(xcoord, ycoord, d);
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean testMove(Direction d) {
		Level level = ((EntanglementStage) stage).level;
		if (!level.inBounds(xcoord + d.getUnitX(), ycoord + d.getUnitY())) {
			return false;
		}
		GriddedEntity other = level.get(xcoord + d.getUnitX(),
				ycoord + d.getUnitY());
		if (other == null) {
			return true;
		} else if (!(other instanceof Block)) {
			return false;
		} else {
			if (other.testMove(d)) {
				return true;
			} else {
				return false;
			}
		}
	}

}
