
package net.jgf.example.tanks.logic;

import java.io.IOException;

import net.jgf.config.Configurable;
import net.jgf.core.naming.ObjectCreator;
import net.jgf.core.naming.Register;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Client;
import net.jgf.example.tanks.messages.ConnectMessage;
import net.jgf.example.tanks.messages.GameInfoMessage;
import net.jgf.logic.BaseLogicState;
import net.jgf.logic.action.LogicAction;
import net.jgf.network.SpiderMonkeyNetworkService;

import org.apache.log4j.Logger;

import com.jme3.network.events.MessageListener;
import com.jme3.network.message.Message;


/**
 *
 */
@Configurable
public class ServerNetworkLogic extends BaseLogicState implements MessageListener {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(ServerNetworkLogic.class);

    @Register (ref = "network")
    private SpiderMonkeyNetworkService network;
    
    @Register (ref = "entity/root/clients")
    private EntityGroup clientGroup; 
    
    private int clientCount = 1;
    
	@Override
    public void doLoad() {
        super.doLoad();
        network.getServer().addMessageListener(this, ConnectMessage.class);
    }

    @Override
    public void messageReceived(Message message) {
        logger.debug ("Received message: " + message);
        if (message instanceof ConnectMessage) {
            messageReceivedConnect((ConnectMessage) message);
        }
    }

    private void messageReceivedConnect (ConnectMessage message) {
        Client player = ObjectCreator.createObject(Client.class);
        player.setId(clientGroup.getId() + "/client" + clientCount++);
        player.setName(message.playerName);
        player.setScore(0);
        clientGroup.attachChild(player);
        
        GameInfoMessage gameInfoMessage = new GameInfoMessage();
        gameInfoMessage.settingsToMessage();
        
        try {
            message.getClient().send(gameInfoMessage);
        } catch (IOException e) {
            logger.debug("Could not send message to client", e);
            // TODO: Drop player
        }
        
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

