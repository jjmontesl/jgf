/**
 * $Id$
 * Java Game Framework
 */

package net.jgf.example.mudvolley1.logic;

import net.jgf.config.Configurable;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.EntityGroup;
import net.jgf.example.mudvolley1.entity.BallEntity;
import net.jgf.example.mudvolley1.entity.PlayerEntity;
import net.jgf.example.mudvolley1.loader.MudVolleyEntityLoader;
import net.jgf.example.mudvolley1.loader.MudVolleySceneLoader;
import net.jgf.jme.scene.DefaultJmeScene;
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
	public void activate() {

		super.activate();

		NewGameLogic.logger.info ("Starting new volley game (logic)");

		// Prepare scene
		DefaultJmeScene scene = Jgf.getDirectory().getObjectAs("scene", DefaultJmeScene.class);
		MudVolleySceneLoader sceneLoader = Jgf.getDirectory().getObjectAs("loader/scene", MudVolleySceneLoader.class);
		sceneLoader.load(scene);

		// Prepare entities
		EntityGroup rootEntity = Jgf.getDirectory().getObjectAs("entity/root", EntityGroup.class);
		MudVolleyEntityLoader entityLoader = Jgf.getDirectory().getObjectAs("loader/entity", MudVolleyEntityLoader.class);

		PlayerEntity player1 = entityLoader.loadPlayer("player1", -1.0f);
		player1.integrate(rootEntity, scene.getRootNode(), new Vector3f(-7, 0, 0));

		PlayerEntity player2 = entityLoader.loadPlayer("player2", 1.0f);
		player2.integrate(rootEntity, scene.getRootNode(), new Vector3f(7, 0, 0));

		BallEntity ball = entityLoader.loadBall("ball");
		ball.integrate(rootEntity, scene.getRootNode());

		scene.getRootNode().updateRenderState();
		StateHelper.loadAndActivate(rootEntity);

		// Activate next view and logic
		LogicState inGameLogic = Jgf.getDirectory().getObjectAs("logic/root/ingame", LogicState.class);
		StateHelper.loadAndActivate(inGameLogic);
		ViewState sceneRenderView = Jgf.getDirectory().getObjectAs("view/root/level", ViewState.class);
		StateHelper.loadAndActivate(sceneRenderView);

	}


	@Override
	public void update(float tpf) {
		// Nothing to do

	}

}
