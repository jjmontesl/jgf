
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.loader.SceneReferencesProcessorLoader;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.LoadProperties;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.scene.SimpleSceneManager;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class SetupAction extends BaseLogicAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(SetupAction.class);

	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(Object arg) {

		SpawnLogic spawnLogic = Jgf.getDirectory().getObjectAs("logic/root/ingame/spawn", SpawnLogic.class);
		SimpleSceneManager sceneManager = Jgf.getDirectory().getObjectAs("scene/manager", SimpleSceneManager.class);
		DefaultJmeScene scene = (DefaultJmeScene) sceneManager.getScene();
		EntityGroup enemies = Jgf.getDirectory().getObjectAs("entity/root/enemy", EntityGroup.class);
		EntityGroup rootEntity = Jgf.getDirectory().getObjectAs("entity/root", EntityGroup.class);
        
		// Load scene
		SceneReferencesProcessorLoader sceneLoader = Jgf.getDirectory().getObjectAs("loader/scene/referencesprocessor", SceneReferencesProcessorLoader.class);
		sceneLoader.load(scene, new LoadProperties());
		
		// Spawn player
		StateHelper.loadAndActivate(spawnLogic);
		spawnLogic.spawnPlayer();

		// Prepare entities
		
		StateHelper.loadAndActivate(rootEntity);

        for (Entity enemy : enemies.children()) {
            StateHelper.loadAndActivate(enemy);
        }

	}

}
