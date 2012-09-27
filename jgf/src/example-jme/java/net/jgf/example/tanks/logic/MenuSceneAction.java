
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.state.StateHelper;
import net.jgf.example.tanks.loader.SceneReferencesProcessorLoader;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.FileChainLoader;
import net.jgf.loader.LoadProperties;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.scene.Scene;
import net.jgf.scene.SimpleSceneManager;
import net.jgf.system.Jgf;

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
		
	    // Load scene
		SceneReferencesProcessorLoader refprocLoader = Jgf.getDirectory().getObjectAs("loader/scene/referencesprocessor", SceneReferencesProcessorLoader.class);
        refprocLoader.load(scene, new LoadProperties());
		
		sceneManager.setScene(scene);
		Jgf.getDirectory().addObject(scene.getId(), scene);

		// Default Nodes
		scene.getRootNode().attachChild(new Node("bullets"));
		
		// Enable view
		StateHelper.loadAndActivate("view/root/main");
		StateHelper.loadAndActivate("view/root/scene");

	}

}
