
package net.jgf.example.gamestatewrapper;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jmex.game.state.DebugGameState;


/**
 * <p>BoxState is a JME GameState based on the TestSimpleGame JME test ({@link jmetest.base.TestSimpleGame}).</p>
 * @see net.jgf.jme.view.GameStateWrapperView
 */
public class BoxGameState extends DebugGameState {

	/**
	 * The box to be shown.
	 */
	private Box box;

	/**
	 * Constructs the GameState and adds a box to its root node.
	 */
	public BoxGameState() {

		// Put our box in it
		box = new Box("my box", new Vector3f(0, 0, 0), 2, 2, 2);
		box.setModelBound(new BoundingSphere());
		box.updateModelBound();

		// We had to add the following line because the render thread is already running
		// Anytime we add content we need to updateRenderState or we get funky effects
		this.getRootNode().attachChild(box);
		box.updateRenderState();

	}

	/* (non-Javadoc)
	 * @see com.jmex.game.state.DebugGameState#update(float)
	 */
	@Override
	public void update(float tpf) {
		super.update(tpf);
		box.getLocalRotation().multLocal(new Quaternion().fromAngleAxis(tpf, Vector3f.UNIT_X));
	}

}
