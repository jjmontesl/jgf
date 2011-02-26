
package net.jgf.example.tanks.logic;

import java.util.concurrent.Callable;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.example.tanks.messages.GameInfoMessage;
import net.jgf.logic.BaseLogicState;
import net.jgf.logic.action.LogicAction;
import net.jgf.network.SpiderMonkeyNetworkService;
import net.jgf.settings.StringSetting;

import org.apache.log4j.Logger;

import com.jme.util.GameTask;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme3.network.events.MessageListener;
import com.jme3.network.message.Message;


/**
 *
 */
@Configurable
public class ClientNetworkLogic extends BaseLogicState implements MessageListener {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(ClientNetworkLogic.class);

    @Register (ref = "network")
    private SpiderMonkeyNetworkService network;
    
    @Register (ref = "logic/action/newgame/init")
    private NewGameAction newGameAction;
    
    @Register (ref = "logic/action/restart")
    private SetupAction restartAction;
    
    @Register (ref = "settings/game/map")
    private StringSetting mapSetting;
    
	@Override
    public void doLoad() {
        super.doLoad();
        network.getClient().addMessageListener(this, GameInfoMessage.class);
    }

    @Override
    public void messageReceived(Message message) {
        
        logger.debug("GameInfoMessage received");
        final GameInfoMessage gameInfoMessage = (GameInfoMessage) message;
        
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).enqueue(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                String previousMap = mapSetting.getValue();
                gameInfoMessage.messageToSettings();
               
                // Load or reload depending on map change
                if (mapSetting.getValue().equals(previousMap)) {
                    newGameAction.loadMission();
                    restartAction.perform(null);
                } else {
                    newGameAction.loadMission();
                    restartAction.perform(null);
                }
                
                return null;
            }
            
        });
        
    }

    @Override
    public void messageSent(Message message) {
    }

    @Override
    public void objectReceived(Object object) {
    }

    @Override
    public void objectSent(Object object) {
    }

}

