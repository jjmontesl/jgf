
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
		
		
		
		EntityGroup rootEntity = Jgf.getDirectory().getObjectAs("entity/root", EntityGroup.class);
		LogicState inGameLogic = Jgf.getDirectory().getObjectAs("logic/root/ingame", LogicState.class);
        ViewState levelView = Jgf.getDirectory().getObjectAs("view/root/level", ViewState.class);
        EffectsView effectsView = Jgf.getDirectory().getObjectAs("view/root/level/fight/effects", EffectsView.class);
        
        if (rootEntity.isLoaded()) rootEntity.deactivate();
        if (inGameLogic.isLoaded()) inGameLogic.deactivate();
        if (levelView.isLoaded()) levelView.deactivate();
        if (effectsView.isLoaded()) effectsView.deactivate();
        
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
		
		// Activate next view and logic
		
		StateHelper.loadAndActivate(inGameLogic);
		StateHelper.loadAndActivate(levelView);
		StateHelper.loadAndActivate(effectsView);
		
        // Clear banner
        ViewState failedView = Jgf.getDirectory().getObjectAs("view/root/level/failed", ViewState.class);
        StateHelper.deactivateAndUnload(failedView);		

	}

}
