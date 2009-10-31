
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.FileChainLoader;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.logic.LogicState;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.scene.Scene;
import net.jgf.scene.SimpleSceneManager;
import net.jgf.system.Jgf;
import net.jgf.view.ViewState;

import org.apache.log4j.Logger;

import com.jme.scene.Node;


/**
 *
 */
@Configurable
public class MenuSceneAction extends BaseLogicAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(MenuSceneAction.class);

	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(Object arg) {

		logger.info ("Starting new tanks game (logic)");

		// Prepare scene
		SimpleSceneManager sceneManager = Jgf.getDirectory().getObjectAs("scene/manager", SimpleSceneManager.class);
		FileChainLoader<Scene> sceneLoader = Jgf.getDirectory().getObjectAs("loader/scene", FileChainLoader.class);
		DefaultJmeScene scene =(DefaultJmeScene) sceneLoader.load(
				null, "FileChainLoader.resourceUrl=tanks/level/intro.xml"
		);
		sceneManager.setScene(scene);
		Jgf.getDirectory().addObject(scene.getId(), scene);

		// Default Nodes
		scene.getRootNode().attachChild(new Node("bullets"));

	}

}
