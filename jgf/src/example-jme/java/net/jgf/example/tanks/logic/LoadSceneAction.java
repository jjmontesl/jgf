
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.example.tanks.loader.SceneReferencesProcessorLoader;
import net.jgf.example.tanks.logic.network.ServerNetworkLogic;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.FileChainLoader;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.scene.Scene;
import net.jgf.scene.SimpleSceneManager;
import net.jgf.settings.StringSetting;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.scene.Node;


/**
 *
 */
@Configurable
public class LoadSceneAction extends BaseLogicAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(LoadSceneAction.class);

    @Register (ref = "scene/manager")
    private SimpleSceneManager sceneManager;
    
    @Register (ref = "loader/scene")
    private FileChainLoader<Scene> sceneLoader;
    
    @Register (ref = "settings/game/map")
    private StringSetting mapSetting;
    
    @Register (ref = "logic/root/network/server")
    protected ServerNetworkLogic serverLogic;
	
	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(Object arg) {

		logger.info ("Loading map: " + mapSetting.getValue());
		
	    // Update network
		if (serverLogic.isActive()) serverLogic.sendGameInfo(null);
		
        EntityPoolLoader entityLoader = Jgf.getDirectory().getObjectAs("loader/entity/pool", EntityPoolLoader.class);
        entityLoader.preload(60, "FileChainLoader.resourceUrl=tanks/entity/bullet.xml");
	    
	    DefaultJmeScene scene =(DefaultJmeScene) sceneLoader.load(
				null, "FileChainLoader.resourceUrl=tanks/level/" + mapSetting.getValue() + ".xml"
		);
		sceneManager.setScene(scene);
		Jgf.getDirectory().addObject("scene", scene);
		
		// Default Nodes
		scene.getRootNode().attachChild(new Node("bullets"));

		// Set a camera
		//SceneRenderView sceneRenderView = Jgf.getDirectory().getObjectAs("view/root/ingame/game/scene", SceneRenderView.class);
		//sceneRenderView.setCamera(scene.getCameraControllers().getCameraController("scene/camera/test"));

	}

}
