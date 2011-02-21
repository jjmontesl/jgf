
package net.jgf.example.mudvolley3.logic;

import net.jgf.config.Configurable;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.EntityGroup;
import net.jgf.example.mudvolley1.entity.BallEntity;
import net.jgf.example.mudvolley1.entity.PlayerEntity;
import net.jgf.example.mudvolley3.camera.MudVolleyCamera;
import net.jgf.example.mudvolley3.entity.MudVolleyEntityLoader;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.scene.SceneLoader;
import net.jgf.logic.BaseLogicState;
import net.jgf.logic.LogicState;
import net.jgf.system.Jgf;
import net.jgf.view.ViewState;

import org.apache.log4j.Logger;

import com.jme.math.Vector3f;





/**
 *
 */
@Configurable
public class NewGameLogic extends BaseLogicState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(NewGameLogic.class);

	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void doActivate() {

		super.doActivate();

		logger.info ("Starting new volley game (logic)");

		// Prepare scene
		DefaultJmeScene scene = Jgf.getDirectory().getObjectAs("scene", DefaultJmeScene.class);
		SceneLoader sceneLoader = Jgf.getDirectory().getObjectAs("loader/scene", SceneLoader.class);
		sceneLoader.load(scene);

		// Prepare entities
		EntityGroup rootEntity = Jgf.getDirectory().getObjectAs("entity/root", EntityGroup.class);
		MudVolleyEntityLoader entityLoader = Jgf.getDirectory().getObjectAs("loader/entity", MudVolleyEntityLoader.class);

		PlayerEntity player1 = entityLoader.loadPlayer("entity/root/player1", -1.0f);
		player1.integrate(rootEntity, scene.getRootNode(), new Vector3f(-7, 0, 0));

		PlayerEntity player2 = entityLoader.loadPlayer("entity/root/player2", 1.0f);
		player2.integrate(rootEntity, scene.getRootNode(), new Vector3f(7, 0, 0));

		BallEntity ball = entityLoader.loadBall("ball");
		ball.setId("entity/root/ball");
		ball.integrate(rootEntity, scene.getRootNode());

		scene.getRootNode().updateRenderState();
		StateHelper.loadAndActivate(rootEntity);

		// Link camera to ball
		MudVolleyCamera camera = Jgf.getDirectory().getObjectAs("scene/cameras/match", MudVolleyCamera.class);
		camera.setTarget(ball.getSpatial());

		// Change states
		ViewState sceneRenderView = Jgf.getDirectory().getObjectAs("view/root/level", ViewState.class);
		StateHelper.loadAndActivate(sceneRenderView);
		LogicState inGameLogic = Jgf.getDirectory().getObjectAs("logic/root/ingame", LogicState.class);
		StateHelper.loadAndActivate(inGameLogic);

	}

	@Override
	public void doUpdate(float tpf) {
		// Nothing to do
	}

}
