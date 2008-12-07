
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.state.StateUtil;
import net.jgf.entity.EntityGroup;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.FileChainLoader;
import net.jgf.logic.LogicState;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.scene.Scene;
import net.jgf.scene.SceneManager;
import net.jgf.system.Jgf;
import net.jgf.view.ViewState;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class NewGameAction extends BaseLogicAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(NewGameAction.class);

	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(String action) {

		logger.info ("Starting new tanks game (logic)");

		// Prepare scene
		SceneManager sceneManager = Jgf.getDirectory().getObjectAs("scene/manager", SceneManager.class);
		FileChainLoader<Scene> sceneLoader = Jgf.getDirectory().getObjectAs("loader/scene", FileChainLoader.class);
		DefaultJmeScene scene =(DefaultJmeScene) sceneLoader.load(
				null, "FileChainLoader.resourceUrl=tanks/level/mission1.xml"
		);
		sceneManager.setScene(scene);
		Jgf.getDirectory().addObject(scene.getId(), scene);

		// Set a camera
		//SceneRenderView sceneRenderView = Jgf.getDirectory().getObjectAs("view/root/level/fight/scene", SceneRenderView.class);
		//sceneRenderView.setCamera(scene.getCameraControllers().getCameraController("scene/camera/test"));

		// Spawn player
		SpawnLogic spawnLogic = Jgf.getDirectory().getObjectAs("logic/root/ingame/spawn", SpawnLogic.class);
		StateUtil.loadAndActivate(spawnLogic);
		spawnLogic.spawnPlayer();

		// Prepare entities
		EntityGroup rootEntity = Jgf.getDirectory().getObjectAs("entity/root", EntityGroup.class);
		StateUtil.loadAndActivate(rootEntity);

		// Activate next view and logic
		LogicState inGameLogic = Jgf.getDirectory().getObjectAs("logic/root/ingame", LogicState.class);
		StateUtil.loadAndActivate(inGameLogic);
		ViewState levelView = Jgf.getDirectory().getObjectAs("view/root/level", ViewState.class);
		StateUtil.loadAndActivate(levelView);

	}

}
