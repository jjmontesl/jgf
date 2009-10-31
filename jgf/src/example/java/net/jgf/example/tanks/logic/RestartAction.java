
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Bullet;
import net.jgf.example.tanks.entity.Tank;
import net.jgf.example.tanks.loader.SceneReferencesProcessorLoader;
import net.jgf.example.tanks.view.EffectsView;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.logic.LogicState;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.scene.SimpleSceneManager;
import net.jgf.system.Jgf;
import net.jgf.view.ViewState;

import org.apache.log4j.Logger;

import com.jme.scene.Node;
import com.jme.system.DisplaySystem;


/**
 *
 */
@Configurable
public class RestartAction extends BaseLogicAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(RestartAction.class);

	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(Object arg) {

		logger.info ("Restarting level (logic)");

		EntityPoolLoader entityLoader = Jgf.getDirectory().getObjectAs("loader/entity/pool", EntityPoolLoader.class);
		SpawnLogic spawnLogic = Jgf.getDirectory().getObjectAs("logic/root/ingame/spawn", SpawnLogic.class);
		SimpleSceneManager sceneManager = Jgf.getDirectory().getObjectAs("scene/manager", SimpleSceneManager.class);
		DefaultJmeScene scene = (DefaultJmeScene) sceneManager.getScene();
		Node bulletNode = (Node) scene.getRootNode().getChild("bullets");
		
		// Clean everything
		EntityGroup bullets = Jgf.getDirectory().getObjectAs("entity/root/bullets", EntityGroup.class);
		for (Entity bullet : bullets.children()) {
			if (bullet.isActive()) {
				((Bullet)bullet).withdraw(bullets, bulletNode);
				StateHelper.deactivateAndUnload(bullet);
				bullet.clearStateObservers();
				entityLoader.returnToPool(bullet);
			}
		}
		EntityGroup enemies = Jgf.getDirectory().getObjectAs("entity/root/enemy", EntityGroup.class);
		for (Entity enemy : enemies.children()) {
			((Tank) enemy).withdraw(enemies, scene.getRootNode());
			StateHelper.deactivateAndUnload(enemy);
		}
		
		SceneReferencesProcessorLoader sceneLoader = Jgf.getDirectory().getObjectAs("loader/scene/referencesprocessor", SceneReferencesProcessorLoader.class);
		sceneLoader.load(scene, new LoadProperties());
		
		// Spawn player
		StateHelper.loadAndActivate(spawnLogic);
		spawnLogic.spawnPlayer();

		// Prepare entities
		EntityGroup rootEntity = Jgf.getDirectory().getObjectAs("entity/root", EntityGroup.class);
		StateHelper.loadAndActivate(rootEntity);

		// Activate next view and logic
		LogicState inGameLogic = Jgf.getDirectory().getObjectAs("logic/root/ingame", LogicState.class);
		StateHelper.loadAndActivate(inGameLogic);
		ViewState levelView = Jgf.getDirectory().getObjectAs("view/root/level", ViewState.class);
		StateHelper.loadAndActivate(levelView);
		
		// Clear banner
		ViewState failedView = Jgf.getDirectory().getObjectAs("view/root/level/failed", ViewState.class);
		failedView.deactivate();
		

	}

}
