
package net.jgf.example.box;

import net.jgf.config.Configurable;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.jme.scene.util.SceneUtils;
import net.jgf.view.BaseViewState;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.system.DisplaySystem;


/**
 * <p>BoxState is a JME GameState based on the TestSimpleGame JME test ({@link jmetest.base.TestSimpleGame}).</p>
 * @see net.jgf.jme.view.GameStateWrapperView
 */
@Configurable
public class BoxView extends BaseViewState {

	/**
	 * The box to be shown.
	 */
	private DefaultJmeScene scene;


	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#load()
	 */
	@Override
	public void doLoad() {
		super.doLoad();

		// Get a scene
		scene = new DefaultJmeScene();
		SceneUtils.createCommonRenderStates(scene);
		scene.getRootNode().setRenderState(scene.getCommonRenderStates().get("zBuffer"));
		scene.getRootNode().setRenderState(SceneUtils.createDefaultLightState());

		// Put our box in it
		Box box = new Box("my box", new Vector3f(0, 0, 0), 2, 2, 2);
		box.setModelBound(new BoundingSphere());
		box.updateModelBound();
		scene.getRootNode().attachChild(box);


		scene.getRootNode().updateRenderState();

	}



	/* (non-Javadoc)
	 * @see com.jmex.game.state.DebugGameState#update(float)
	 */
	@Override
	public void doUpdate(float tpf) {
		super.doUpdate(tpf);
		scene.getRootNode().getLocalRotation().multLocal(new Quaternion().fromAngleAxis(tpf, new Vector3f(1.0f, 1.0f, 1.0f))).normalize();
		scene.update(tpf);
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#render(float)
	 */
	@Override
	public void doRender(float tpf) {
		super.doRender(tpf);
		DisplaySystem.getDisplaySystem().getRenderer().draw(scene.getRootNode());
	}



}
