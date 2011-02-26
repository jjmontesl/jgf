
package net.jgf.example.tanks.logic;

import java.io.IOException;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.StateHelper;
import net.jgf.example.tanks.messages.ConnectMessage;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.FileChainLoader;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.network.SpiderMonkeyNetworkService;
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
public class JoinGameAction extends BaseLogicAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(JoinGameAction.class);

    @Register (ref = "network")
    private SpiderMonkeyNetworkService network;
    
    @Register (ref = "settings/game/playername")
    private StringSetting playernameSetting;
	
    
    
	@Override
    public void doLoad() {
        super.doLoad();
    }



    /* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(Object arg) {

		logger.info ("Starting client");
		
		ConnectMessage connectMessage = new ConnectMessage();
		connectMessage.playerName = playernameSetting.getValue();
		
		try {
		    network.connect();
		    StateHelper.loadAndActivate("logic/root/network/client");
		    network.getClient().send(connectMessage);
		} catch (IOException e) {
		    logger.error("Could not start network connection to server.", e);
		    // TODO: Popup informing
		}
		
	}

}
