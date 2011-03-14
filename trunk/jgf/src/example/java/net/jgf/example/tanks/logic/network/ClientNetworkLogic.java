
package net.jgf.example.tanks.logic.network;

import java.io.IOException;
import java.util.concurrent.Callable;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Bullet;
import net.jgf.example.tanks.entity.Player;
import net.jgf.example.tanks.entity.Tank;
import net.jgf.example.tanks.logic.InGameLogic;
import net.jgf.example.tanks.logic.SpawnLogic;
import net.jgf.example.tanks.messages.BulletMessage;
import net.jgf.example.tanks.messages.ControlsMessage;
import net.jgf.example.tanks.messages.GameInfoMessage;
import net.jgf.example.tanks.messages.PlayerMessage;
import net.jgf.example.tanks.messages.SyncRequestMessage;
import net.jgf.example.tanks.messages.TankMessage;
import net.jgf.example.tanks.view.EffectsView;
import net.jgf.jme.model.util.TransientSavable;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.logic.BaseLogicState;
import net.jgf.logic.action.LogicAction;
import net.jgf.network.ActionStepMessage;
import net.jgf.network.SpiderMonkeyNetworkService;
import net.jgf.settings.StringSetting;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
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
    
    @Register (ref = "logic/action/menu/disable")
    private LogicAction disableAction;
    
    @Register (ref = "logic/action/game/loadscene")
    private LogicAction loadSceneAction;
    
    @Register (ref = "logic/action/game/enable")
    private LogicAction enableAction;
    
    @Register (ref = "logic/action/game/cleanup")
    private LogicAction cleanupAction;
    
    @Register (ref = "settings/game/map")
    private StringSetting mapSetting;
    
    @Register (ref = "logic/root/ingame/spawn")
    protected SpawnLogic spawnLogic;
    
    @Register (ref = "logic/root/ingame/ingame")
    protected InGameLogic ingameLogic;
    
    @Register (ref = "loader/entity/pool")
    protected EntityPoolLoader entityLoader;
    
    @Register (ref = "entity/root/tanks")
    protected EntityGroup tanks;
    
    @Register (ref = "entity/root/players")
    protected EntityGroup players;
    
    @Register (ref = "entity/root")
    protected EntityGroup entities;
    
    @Register (ref = "scene")
    protected DefaultJmeScene scene;
    
    @Register (ref = "entity/root/bullets")
    protected EntityGroup bullets;
    
    @Register (ref="entity/root/links/self")
    private net.jgf.example.tanks.entity.Player self;
    
    @Register (ref = "view/root/scene/effects")
    protected EffectsView effectsView;
    
    public static final float updatePeriod = 0.1f;
    
    private float timeSinceUpdate = 0.0f;
    
	@Override
    public void doLoad() {
        super.doLoad();
        network.getClient().addMessageListener(this, GameInfoMessage.class);
        network.getClient().addMessageListener(this, PlayerMessage.class);
        network.getClient().addMessageListener(this, TankMessage.class);
        network.getClient().addMessageListener(this, BulletMessage.class);
        network.getClient().addMessageListener(this, ActionStepMessage.class);
    }

	@Override
    public void doUpdate(float tpf) {
        super.doUpdate(tpf);

        timeSinceUpdate += tpf;
        
        if (timeSinceUpdate > updatePeriod) {
            
            timeSinceUpdate = 0.0f;
            
            if (self == null) return;
            if (self.getTank() == null) return;
        
            try {
                
                ControlsMessage controlsMessage = new ControlsMessage();
                controlsMessage.direction = ((net.jgf.example.tanks.entity.Tank)self.getTank()).getDirection();
                controlsMessage.fire = ((net.jgf.example.tanks.entity.Tank)self.getTank()).isFiring();
                controlsMessage.target = ((net.jgf.example.tanks.entity.Tank)self.getTank()).getTarget();
                
                
                network.getClient().send(controlsMessage);
                
                ((net.jgf.example.tanks.entity.Tank)self.getTank()).setFiring(false);
            
            } catch (IOException e) {
                // TODO: Check if this breaks broadcasting to correct clients
                // TODO: Do something with exception
            }

        }
        
        
    }
	
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
    public void messageReceived(Message message) {
        
        logger.trace ("Received message: " + message);
        
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).enqueue(new MessageCallable(message));
        
    }
    
    private void messageReceivedProcess(Message message) {
        if (message instanceof GameInfoMessage) {
            messageReceivedGameInfo((GameInfoMessage) message);
        } else if (message instanceof PlayerMessage) {
            messageReceivedPlayer((PlayerMessage) message);
        } else if (message instanceof TankMessage) {
            messageReceivedTank((TankMessage) message);
        } else if (message instanceof BulletMessage) {
            messageReceivedBullet((BulletMessage) message);
        } else if (message instanceof ActionStepMessage) {
            messageReceivedActionStep((ActionStepMessage) message);
        }
        
    }

    public void messageReceivedActionStep(ActionStepMessage message) {
        message.perform(null);
    }
    
    public void messageReceivedBullet(BulletMessage message) {
        Bullet bullet = (Bullet) bullets.findChild(message.bulletId);
        if (bullet == null) {
            
            if (message.destroy) return;
            
            bullet = (Bullet) entityLoader.load(null, "FileChainLoader.resourceUrl=tanks/entity/bullet.xml");
            bullet.setId(message.bulletId);
            bullet.clearStateObservers();

            // TODO: Model Bounds don't quite belong to logic...
            // maybe to the entity or better yet, to loader
            bullet.getSpatial().setModelBound(new BoundingSphere());
            bullet.getSpatial().updateModelBound();

            Node bulletNode = (Node) scene.getRootNode().getChild("bullets");
            bullet.integrate(bullets, bulletNode, message.translation);
            bullet.getSpatial().setUserData("entity", new TransientSavable<Entity>(bullet));

            StateHelper.loadAndActivate(bullet);
            //bullet.getSpatial().updateRenderState();
            scene.getRootNode().updateRenderState();

            Vector3f orientation = message.hitPosition.subtract(message.translation).normalizeLocal();
            Vector3f newPosition = message.translation.clone();
            //newPosition.addLocal(orientation.normalizeLocal().multLocal(1.10f));
            newPosition.y = 0.6f;
            bullet.startFrom(newPosition, orientation);

            effectsView.addBullet(bullet);
            StateHelper.loadAndActivate(bullet);
        }
        
        if (message.destroy) {
            spawnLogic.destroyBullet(bullet);
        }
        
    }
    
    public void messageReceivedTank(TankMessage message) {
        Tank tank = null;
        if (Jgf.getDirectory().containsObject(message.tankId)) {
            tank = Jgf.getDirectory().getObjectAs(message.tankId, Tank.class);
        } else {
            if ((self == null)) return;
            
            if (message.playerId != null) {
                if (Jgf.getDirectory().getObjectAs(message.playerId, Player.class) == null) {
                    return;
                }
            }
            
            LoadProperties loadProperties = new LoadProperties();
            if (self.getId().equals(message.playerId)) {
                loadProperties.put("FileChainLoader.resourceUrl", "tanks/entity/tank.xml");
            } else {
                loadProperties.put("FileChainLoader.resourceUrl", "tanks/entity/remotetank.xml");
            }
            loadProperties.put("ConverterLoader.resourceUrl", message.tankModel);
            tank = (Tank) entityLoader.load(null, loadProperties);
            tank.setId(message.tankId);
            Node tankNode = ((Node)((Node)tank.getSpatial()).getChild("Tank"));
            Spatial hull = tankNode.getChild("Hull");
            // TODO: Model Bounds don't quite belong to logic... may be better in loader?
            BoundingBox obb = new BoundingBox();
            hull.setModelBound(obb);
            hull.updateModelBound();

            tank.integrate(tanks, scene.getRootNode(), message.translation);
            scene.getRootNode().updateRenderState();
            
            StateHelper.loadAndActivate(tank);
        } 
        tank.getSpatial().setLocalTranslation(message.translation);
        tank.getDirection().set(message.direction);
        tank.getTarget().set(message.target);
        if (message.playerId != null) {
            Player player = Jgf.getDirectory().getObjectAs(message.playerId, Player.class);
            player.setTank(tank);
            tank.setPlayer(player);
        }
            
        if (message.destroy) {
            spawnLogic.destroyTank(tank, null);
        }
        // TODO: Set Player if appropriate
    }
    
    public void messageReceivedPlayer(PlayerMessage message) {
        Player player = null;
        boolean isNewPlayer = false;
        if (Jgf.getDirectory().containsObject(message.playerId)) {
            player = Jgf.getDirectory().getObjectAs(message.playerId, Player.class);
        } else {
            isNewPlayer = true;
            player = new Player();
        }
        
        player.setId(message.playerId);
        player.setName(message.playerName);
        player.setScore(message.playerScore);
        if (isNewPlayer) {
            Jgf.getDirectory().addObject(player.getId(), player);
            
        }
        
        if (players.children().size() == 0) {
            Jgf.getDirectory().addObject("entity/root/links/self", player);
        }
        
        if (isNewPlayer) players.attachChild(player);
        
        
        
    }
        
    public void messageReceivedGameInfo(GameInfoMessage message) {

        final GameInfoMessage gameInfoMessage = message;
        
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).enqueue(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                String previousMap = mapSetting.getValue();
                gameInfoMessage.messageToSettings();
               
                disableAction.perform(null);
                cleanupAction.perform(null);
                
                // Load or reload depending on map change
                if (! mapSetting.getValue().equals(previousMap)) {
                    loadSceneAction.perform(null);
                }
                    
                enableAction.perform(null);
                
                // Request initial snapshot
                SyncRequestMessage snapshotMessage = new SyncRequestMessage();
                network.getClient().send(snapshotMessage);

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

