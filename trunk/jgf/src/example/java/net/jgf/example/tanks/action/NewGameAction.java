
package net.jgf.example.tanks.action;

import net.jgf.action.BaseAction;
import net.jgf.config.Configurable;
import net.jgf.core.state.StateUtil;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.logic.SpawnLogic;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.jme.view.SceneRenderView;
import net.jgf.loader.FileChainLoader;
import net.jgf.logic.LogicState;
import net.jgf.scene.Scene;
import net.jgf.scene.SceneManager;
import net.jgf.system.System;
import net.jgf.view.ViewState;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class NewGameAction extends BaseAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(NewGameAction.class);

	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform() {

		logger.info ("Starting new tanks game (logic)");

		// Prepare scene
		SceneManager sceneManager = System.getDirectory().getObjectAs("scene/manager", SceneManager.class);
		FileChainLoader<Scene> sceneLoader = System.getDirectory().getObjectAs("loader/scene", FileChainLoader.class);
		DefaultJmeScene scene =(DefaultJmeScene) sceneLoader.load(
				"FileChainLoader.resourceUrl=tanks/level/mission1.xml"
		);
		sceneManager.setScene(scene);
		System.getDirectory().addObject(scene.getId(), scene);

		// Set a camera
		SceneRenderView sceneRenderView = System.getDirectory().getObjectAs("view/root/level/fight/scene", SceneRenderView.class);
		sceneRenderView.setCamera(scene.getCameraControllers().getCameraController("scene/camera/test"));

		// Spawn player
		SpawnLogic spawnLogic = System.getDirectory().getObjectAs("logic/root/ingame/spawn", SpawnLogic.class);
		StateUtil.loadAndActivate(spawnLogic);
		spawnLogic.spawnPlayer();

		// Prepare entities
		EntityGroup rootEntity = System.getDirectory().getObjectAs("entity/root", EntityGroup.class);
		StateUtil.loadAndActivate(rootEntity);

		// Activate next view and logic
		LogicState inGameLogic = System.getDirectory().getObjectAs("logic/root/ingame", LogicState.class);
		StateUtil.loadAndActivate(inGameLogic);
		ViewState levelView = System.getDirectory().getObjectAs("view/root/level", ViewState.class);
		StateUtil.loadAndActivate(levelView);

	}

}
