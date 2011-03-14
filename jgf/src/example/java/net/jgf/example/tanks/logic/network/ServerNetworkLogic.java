
package net.jgf.example.tanks.logic.network;

import java.io.IOException;
import java.util.concurrent.Callable;

import net.jgf.config.Configurable;
import net.jgf.core.naming.ObjectCreator;
import net.jgf.core.naming.Register;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Bullet;
import net.jgf.example.tanks.entity.Player;
import net.jgf.example.tanks.entity.Tank;
import net.jgf.example.tanks.logic.GameMode;
import net.jgf.example.tanks.logic.flow.FreeForAllLogic;
import net.jgf.example.tanks.logic.flow.MissionLogic;
import net.jgf.example.tanks.messages.BulletMessage;
import net.jgf.example.tanks.messages.ConnectMessage;
import net.jgf.example.tanks.messages.ControlsMessage;
import net.jgf.example.tanks.messages.GameInfoMessage;
import net.jgf.example.tanks.messages.PlayerMessage;
import net.jgf.example.tanks.messages.SyncRequestMessage;
import net.jgf.example.tanks.messages.TankMessage;
import net.jgf.logic.BaseLogicState;
import net.jgf.logic.action.control.ActionStep;
import net.jgf.network.ActionStepMessage;
import net.jgf.network.SpiderMonkeyNetworkService;
import net.jgf.settings.StringSetting;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme3.network.connection.Client;
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
	
	public static final float updatePeriod = 0.1f;
	
	private float timeSinceUpdate = 0.0f;

    @Register (ref = "network")
    private SpiderMonkeyNetworkService network;
    
    @Register (ref = "entity/root/players")
    private EntityGroup players; 
    
    @Register (ref = "entity/root/tanks")
    private EntityGroup tanks;
    
    @Register (ref = "entity/root/enemies")
    private EntityGroup enemies;
    
    @Register (ref = "entity/root/bullets")
    private EntityGroup bullets;
    
    @Register (ref = "entity/root")
    private EntityGroup entities;
    
    @Register (ref = "logic/root/flow/mission")
    private MissionLogic missionLogic;
    
    @Register (ref = "logic/root/flow/ffa")
    private FreeForAllLogic ffaLogic;
    
    @Register (ref = "settings/game/mode")
    private StringSetting gamemodeSetting;
    
    private int clientCount = 0;
    

    private class MessageCallable implements Callable<Object> {

        public Message message = null;

        public MessageCallable(Message message) {
            this.message = message;
        }

        @Override
        public Object call() throws Exception {
            messageReceivedProcess(message);
            return null;
        }
    }
    
	@Override
    public void doActivate() {
        super.doActivate();
        network.getServer().addMessageListener(this, ConnectMessage.class);
        network.getServer().addMessageListener(this, SyncRequestMessage.class);
        network.getServer().addMessageListener(this, ControlsMessage.class);
    }

	
	
    @Override
    public void doUpdate(float tpf) {
        super.doUpdate(tpf);

        timeSinceUpdate += tpf;
        
        if (timeSinceUpdate > updatePeriod) {
        
            for (Entity child : tanks.children()) {
                sendTank(null, (Tank) child, false);
            }
            for (Entity child : enemies.children()) {
                sendTank(null, (Tank) child, false);
            }

            timeSinceUpdate = 0.0f;
            
        }
        
    }

    @Override
    public void messageReceived(Message message) {
        
        logger.trace ("Received message: " + message);
        
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).enqueue(new MessageCallable(message));
        
    }
    
    public void messageReceivedProcess(Message message) {
        logger.trace ("Received message: " + message);
        if (message instanceof SyncRequestMessage) {
            messageReceivedRequestSnapshot((SyncRequestMessage) message);
        } else if (message instanceof ConnectMessage) {
            messageReceivedConnect((ConnectMessage) message);
        } else if (message instanceof ControlsMessage) {
            messageReceivedControls((ControlsMessage) message);
        }
    }

    private void messageReceivedRequestSnapshot (SyncRequestMessage message) {
        sendPlayer(null, findMessagePlayer(message));
        GameMode gameMode = GameMode.valueOf(gamemodeSetting.getValue());
        if (gameMode == GameMode.Missions) {
            missionLogic.newPlayer(findMessagePlayer(message));
        } else if (gameMode == GameMode.FreeForAll) {
            ffaLogic.newPlayer(findMessagePlayer(message));
        }
        sendSnapshot(message.getClient());
    }
    
    private Player findMessagePlayer(Message message) {
        for (Entity playerEntity : players.children()) {
            Player player = (Player) playerEntity;
            if (player.getClient() == message.getClient()) {
                return player;
            }
        }
        return null;
    }
    
    private void messageReceivedControls(ControlsMessage message) {
        Player player = findMessagePlayer(message);
        if (player != null) {
            player.getTank().getDirection().set(message.direction);
            player.getTank().getTarget().set(message.target);
            player.getTank().setFiring(message.fire);
        }
    }
    
    private void sendSnapshot (Client client) {
        for (Entity child : players.children()) {
            sendPlayer (client, (Player)child);
        }
        for (Entity child : tanks.children()) {
            sendTank (client, (Tank)child, true);
        }
        for (Entity child : enemies.children()) {
            sendTank (client, (Tank)child, true);
        }
        for (Entity child : bullets.children()) {
            sendBullet (client, (Bullet)child);
        }
    }
    
    public void sendPlayer (Client client, Player player) {
        PlayerMessage playerMessage = new PlayerMessage();
        playerMessage.playerId = player.getId();
        playerMessage.playerName = player.getName();
        playerMessage.playerScore = player.getScore();
        try {
            if (client != null) {
                client.send(playerMessage);
            } else {
                network.getServer().broadcast(playerMessage);
            }
        } catch (IOException e) {
            // TODO: Handle disconnected player
        }
    }
    
    public void sendTank (Client client, Tank tank,boolean reliable) {
        TankMessage tankMessage = new TankMessage(tank);
        tankMessage.setReliable(reliable);
        try {
            if (client != null) {
                client.send(tankMessage);
            } else {
                network.getServer().broadcast(tankMessage);
            }
        } catch (IOException e) {
            // TODO: Handle disconnected player
        }
    }
    
    public void sendBullet (Client client, Bullet bullet) {
        BulletMessage bulletMessage = new BulletMessage();
        
        bulletMessage.bulletId = bullet.getId();
        bulletMessage.translation = bullet.getSpatial().getLocalTranslation();
        bulletMessage.numBounces = bullet.getNumBounces();
        bulletMessage.hitPosition = bullet.getTrip().hitPosition;
        bulletMessage.destroy = ! bullet.isActive();
        
        try {
            if (client != null) {
                client.send(bulletMessage);
            } else {
                network.getServer().broadcast(bulletMessage);
            }
        } catch (IOException e) {
            // TODO: Handle disconnected player
        }
    }
    
    public void sendActionStep (Client client, ActionStep step) {
        
        ActionStepMessage message = new ActionStepMessage(step);
        
        try {
            if (client != null) {
                client.send(message);
            } else {
                network.getServer().broadcast(message);
            }
        } catch (IOException e) {
            // TODO: Handle disconnected player
        }
    }    
    
    public void sendGameInfo (Client client) {
        GameInfoMessage message = new GameInfoMessage();
        message.settingsToMessage();
        try {
            if (client != null) {
                client.send(message);
            } else {
                network.getServer().broadcast(message);
            }
        } catch (IOException e) {
            // TODO: Handle disconnected player
        }
    }
    
    private void messageReceivedConnect (ConnectMessage message) {
        
        // TODO: Check client limit
        
        Player player = ObjectCreator.createObject(Player.class);
        if (players.children().size() > clientCount) clientCount = players.children().size();
        player.setId(players.getId() + "/player" + (++clientCount));
        player.setName(message.playerName);
        player.setScore(0);
        player.setClient(message.getClient());
        players.attachChild(player);
        Jgf.getDirectory().addObject(player.getId(), player);
        
        sendGameInfo(message.getClient());
        
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

