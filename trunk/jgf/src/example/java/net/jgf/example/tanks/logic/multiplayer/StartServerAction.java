
package net.jgf.example.tanks.logic.multiplayer;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.example.tanks.logic.MissionLogic;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.FileChainLoader;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.network.SpiderMonkeyNetworkService;
import net.jgf.scene.Scene;
import net.jgf.scene.SimpleSceneManager;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.scene.Node;


/**
 *
 */
@Configurable
public class StartServerAction extends BaseLogicAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(StartServerAction.class);

    @Register (ref = "network")
    private SpiderMonkeyNetworkService network;
	
	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(Object arg) {

		logger.info ("Starting server");
		network.startServer();
		
	}

}
