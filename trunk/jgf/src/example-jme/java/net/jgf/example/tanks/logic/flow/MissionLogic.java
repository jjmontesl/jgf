
package net.jgf.example.tanks.logic.flow;

import java.io.IOException;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.State;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Bullet;
import net.jgf.example.tanks.entity.Player;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.example.tanks.loader.SceneReferencesProcessorLoader;
import net.jgf.example.tanks.logic.SpawnLogic;
import net.jgf.example.tanks.logic.SpawnLogic.PlayerSpawn;
import net.jgf.example.tanks.logic.network.ServerNetworkLogic;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.LoadProperties;
import net.jgf.logic.action.control.ActionStep;
import net.jgf.logic.action.control.ActionStepType;
import net.jgf.settings.StringSetting;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class MissionLogic extends BaseFlowLogic {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(MissionLogic.class);
	
	@Register (ref = "entity/root/tanks")
	protected EntityGroup tanks;
	
	@Register (ref = "view/root/ingame/failed")
	protected State stateFailed;

	@Register (ref = "view/root/ingame/victory")
    protected State stateVictory;
	
	@Register (ref = "entity/root/enemies")
	protected EntityGroup enemies;
	
    @Register (ref = "entity/root/bullets")
    protected EntityGroup bullets;
    
    @Register (ref = "entity/root")
    protected EntityGroup rootEntity;
    
    @Register (ref = "entity/root/players")
    protected EntityGroup players;
    
    @Register (ref = "logic/root/ingame/spawn")
    protected SpawnLogic spawnLogic;
    
    @Register (ref = "entity/root/links/self")
    protected Player self;
    
    @Register (ref = "settings/game/map")
    private StringSetting mapSetting;
    
    @Register (ref = "logic/root/network/server")
    protected ServerNetworkLogic serverLogic;
    
    @Register (ref = "scene")
    protected DefaultJmeScene scene;
    
	protected boolean fighting;
	
    protected float gameTime;
    
    protected int mission = 1;
    
    protected int lives = 3;
    
    protected PlayerSpawn lastSpawnType = PlayerSpawn.PLAYER1;
    
	@Override
    public void doActivate() {
        super.doActivate();
        fighting = true;
        gameTime = 0;
    }
	
	

    @Override
	public void doUpdate(float tpf) {
		
        if (fighting) {
            gameTime += tpf;

            checkPlayersAlive(tpf);
    		
    		checkMissionFinished(tpf);
		
        }
		
	}
	
    
    
    @Override
    public void doLoad() {
        super.doLoad();
        this.setMission(1);
        mapSetting.setValue("mission" + this.getMission());
    }

    
	private void checkPlayersAlive(float tpf) {
		
	    int playerCount = tanks.children().size();
	    
		if ((playerCount == 0) && (fighting == true)) {
			
			// Players were removed from entities (must have died)
		    fighting = false;
		    StateHelper.loadAndActivate(stateFailed);
		    if (serverLogic.isActive()) serverLogic.sendActionStep(null, new ActionStep(ActionStepType.loadAndActivate, "view/root/ingame/failed"));
		
		}
		
	}
	
	private void checkMissionFinished(float tpf) {
        
	    if ((enemies.children().size() == 0) && (fighting == true)) {
            
	        logger.info("Mission finished with victory");
	        
            // All enemies destroyed
	        fighting = false;
	        
	        // Destroy all bullets
	        for (Entity entity : bullets.children()) {
	            Bullet bullet = (Bullet) entity;
	            spawnLogic.destroyBullet(bullet);
	        }
	        
	        // Enable victory screen
            StateHelper.loadAndActivate(stateVictory);
            if (serverLogic.isActive()) serverLogic.sendActionStep(null, new ActionStep(ActionStepType.loadAndActivate, stateVictory.getId()));
            
        }
        
    }

    @Override
    public void doDeactivate() {
        // TODO Auto-generated method stub
        fighting = false;
    }

    public boolean isFighting() {
        return fighting;
    }

    public float getGameTime() {
        return gameTime;
    }

    public int getMission() {
        return mission;
    }

    public void setMission(int mission) {
        this.mission = mission;
    }



    @Override
    public void newPlayer(Player player) {
        
        if (tanks.children().size() >= 2) {
            try {
                player.getClient().disconnect();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.warn ("Could not disconnect remote client");
            }
            return;
        }
        
        // Player
        Jgf.getDirectory().addObject(player.getId(), player);
        
        serverLogic.sendPlayer(null, player);
        
        // Spawn tank
        lastSpawnType = (lastSpawnType == PlayerSpawn.PLAYER1) ? PlayerSpawn.PLAYER2 : PlayerSpawn.PLAYER1;
        PlayerTank tank = spawnLogic.spawnTank("entity/root/tanks/tank2", lastSpawnType);
        tank.setPlayer(player);
        if (serverLogic.isActive()) serverLogic.sendTank(null, tank, true);
        
    }



    @Override
    public void setup() {
        
        // Load references (enemies) 
        SceneReferencesProcessorLoader sceneLoader = Jgf.getDirectory().getObjectAs("loader/scene/referencesprocessor", SceneReferencesProcessorLoader.class);
        sceneLoader.load(scene, new LoadProperties());
            
        // Spawn tank
        StateHelper.loadAndActivate(spawnLogic);
        
        lastSpawnType = (Math.random() > 0.5) ? PlayerSpawn.PLAYER2 : PlayerSpawn.PLAYER1;
        PlayerTank tank = spawnLogic.spawnTank("entity/root/tanks/tank1", lastSpawnType);
        tank.setPlayer(self);
        
        if (players.children().size() == 2) {
            // Spawn player2 tank
            lastSpawnType = (lastSpawnType == PlayerSpawn.PLAYER1) ? PlayerSpawn.PLAYER2 : PlayerSpawn.PLAYER1;
            PlayerTank tank2 = spawnLogic.spawnTank("entity/root/tanks/tank2", lastSpawnType);
            tank2.setPlayer(Jgf.getDirectory().getObjectAs("entity/root/players/player2", Player.class));
            if (serverLogic.isActive()) serverLogic.sendTank(null, tank2, true);
        }
        
        
        if (serverLogic.isActive()) serverLogic.sendTank(null, tank, true);
        
        // Prepare entities
        StateHelper.loadAndActivate(rootEntity);

        for (Entity enemy : enemies.children()) {
            StateHelper.loadAndActivate(enemy);
        }        
        
        if (!fighting) {
            fighting = true;
            mapSetting.setValue("mission" + mission);
        }
        
    }



    @Override
    public void nextMap() {
        // Increment mission number
        this.mission++;
    }

}

