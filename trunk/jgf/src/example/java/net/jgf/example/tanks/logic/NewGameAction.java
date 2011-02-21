
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.FileChainLoader;
import net.jgf.loader.entity.pool.EntityPoolLoader;
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
public class NewGameAction extends BaseLogicAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(NewGameAction.class);

    @Register (ref = "logic/root/ingame/mission")
    private MissionLogic missionLogic;
    
    @Register (ref = "scene/manager")
    private SimpleSceneManager sceneManager;
    
    @Register (ref = "loader/scene")
    private FileChainLoader<Scene> sceneLoader;
	
	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(Object arg) {

		logger.info ("Starting new tanks game (logic)");
		missionLogic.setMission(1);
	    EntityPoolLoader entityLoader = Jgf.getDirectory().getObjectAs("loader/entity/pool", EntityPoolLoader.class);
	    entityLoader.preload(60, "FileChainLoader.resourceUrl=tanks/entity/bullet.xml");
	    
	    loadMission();
	}
	
	public void loadMission() {
		
	    DefaultJmeScene scene =(DefaultJmeScene) sceneLoader.load(
				null, "FileChainLoader.resourceUrl=tanks/level/mission" + missionLogic.getMission() + ".xml"
		);
		sceneManager.setScene(scene);
		Jgf.getDirectory().addObject("scene", scene);

		// Default Nodes
		scene.getRootNode().attachChild(new Node("bullets"));


		// Set a camera
		//SceneRenderView sceneRenderView = Jgf.getDirectory().getObjectAs("view/root/level/fight/scene", SceneRenderView.class);
		//sceneRenderView.setCamera(scene.getCameraControllers().getCameraController("scene/camera/test"));

	}

}
