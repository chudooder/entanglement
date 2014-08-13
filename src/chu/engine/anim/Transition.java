package chu.engine.anim;

import net.Entanglement;
import chu.engine.Entity;
import chu.engine.Stage;

/**
 * Manages a delayed transition between two stages.
 * @author Shawn
 *
 */
public abstract class Transition extends Entity {
	
	protected Stage to;
	
	public Transition(Stage to) {
		super(0,0);
		this.to = to;
	}
	
	public void done() {
		destroy();
		stage.processRemoveStack();
		Entanglement.setCurrentStage(to);
	}
}
